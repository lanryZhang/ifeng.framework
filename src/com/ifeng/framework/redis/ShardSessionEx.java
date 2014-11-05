package com.ifeng.framework.redis;

import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.ifeng.framework.util.ConfigManager;

public class ShardSessionEx implements ISession {
	private String redisName;
	private ShardedJedis shardedJedis;
	private String sessionName;
	private ShardedJedisPool shardedJedisPool;
	private boolean getJedisInstance = true;
	
	public ShardSessionEx(String name) {
		this.redisName = name;
		sessionName = ConfigManager.getSettings().get("sessionName").toString();
		initShardJedisPool();
	}

	public void initShardJedisPool() {
		if (shardedJedisPool == null) {
			shardedJedisPool = Redis.getShardedJedisPool(redisName);
		}
	}

	private void getResource() throws Exception {
		try {
			shardedJedis = shardedJedisPool.getResource();
			getJedisInstance = true;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public <T> T session(HttpServletRequest request, String key) throws Exception {
		try {
			getResource();
			String sessionKey = "";
			Cookie[] cookies = request.getCookies();
			if (cookies == null)
				return null;
			for (Cookie cookie : cookies) {
				if (cookie.getName().toLowerCase().equals(sessionName.toLowerCase())) {
					sessionKey = URLDecoder.decode(cookie.getValue(),"UTF-8");
					break;
				}
			}
			if (sessionKey.equals("")) {
				return null;
			}

			Map<byte[], byte[]> map = shardedJedis.hgetAll(RedisSerialize.serialize(sessionKey));
			byte[] keys = RedisSerialize.serialize(key);
			byte[] value = map.get(keys);
			if (value == null || value.length == 0) {
				return null;
			} else {
				return RedisSerialize.deserialize(value);
			}
		} catch (Exception e) {
			getJedisInstance = false;
			returnBrokenResource();
			throw e;
		} finally {
			if (getJedisInstance){
				returnResource();
			}
		}

	}
	@Override
	public void returnResource() {
		if (shardedJedis != null) {
			shardedJedisPool.returnResource(shardedJedis);
		}

	}
	
	@Override
	public <T> void session(HttpServletRequest request, String key, T value) throws Exception {
		try {
			getResource();
			String sessionKey = "";
			Cookie[] cookies = request.getCookies();
			if (cookies == null)
				return;
			for (Cookie cookie : cookies) {
				if (cookie.getName().toLowerCase().equals(sessionName.toLowerCase())) {
					sessionKey = URLDecoder.decode(cookie.getValue(),"UTF-8");
					break;
				}
			}
			if (sessionKey.equals("")) {
				return;
			}
			Map<byte[], byte[]> map = shardedJedis.hgetAll(RedisSerialize.serialize(sessionKey));
			byte[] keys = RedisSerialize.serialize(key);
			if (map.containsKey(keys)) {
				map.remove(keys);
			}

			map.put(keys, RedisSerialize.serialize(value));

			shardedJedis.hmset(RedisSerialize.serialize(sessionKey), map);
			shardedJedis.expire(RedisSerialize.serialize(sessionKey), 30 * 60);
		} catch (Exception e) {
			getJedisInstance = false;
			returnBrokenResource();
			throw e;
		} finally {
			if (getJedisInstance){
				returnResource();
			}
		}

	}

	@Override
	public void clearSession(HttpServletRequest request) throws Exception {
		try {
			getResource();
			String sessionKey = "";
			Cookie[] cookies = request.getCookies();
			if (cookies == null)
				return;
			for (Cookie cookie : cookies) {
				if (cookie.getName().toLowerCase().equals(sessionName.toLowerCase())) {
					sessionKey = URLDecoder.decode(cookie.getValue(),"UTF-8");
					break;
				}
			}
			if (sessionKey.equals("")) {
				return;
			}
			shardedJedis.del(RedisSerialize.serialize(sessionKey));
		} catch (Exception e) {
			getJedisInstance = false;
			returnBrokenResource();
			throw e;
		} finally {
			if (getJedisInstance){
				returnResource();
			}
		}
	}

	@Override
	public void returnBrokenResource() {
		if (shardedJedis != null) {
			shardedJedisPool.returnBrokenResource(shardedJedis);
		}
	}
}

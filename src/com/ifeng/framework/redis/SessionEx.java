package com.ifeng.framework.redis;

import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.ifeng.framework.util.ConfigManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class SessionEx implements ISession{
	private String redisName;
	private Jedis jedis;
	private JedisPool jedisPool;
	private String sessionName = "";
	private int timeOut = 30 * 60;
	private boolean getJedisInstance = true;
	
	public SessionEx(String name) {
		this.redisName = name;
		initJedisPool();
		sessionName = ConfigManager.getSettings().get("sessionName").toString();
		if (ConfigManager.getSettings().get("sessionTimeout") != null){
			timeOut = Integer.parseInt(ConfigManager.getSettings().get("sessionTimeout").toString()) * 60;
		}
	}

	public void initJedisPool() {
		if (jedisPool == null) {
			jedisPool = Redis.getJedisPool(redisName);
		}
	}

	private void getResource() throws Exception {
		try {
			jedis = jedisPool.getResource();
			getJedisInstance = true;
		}
		catch (Exception e) {
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

			Map<byte[], byte[]> map = jedis.hgetAll(RedisSerialize.serialize(sessionKey));
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
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}

	}
	@Override
	public void returnBrokenResource() {
		if (jedis != null) {
			jedisPool.returnBrokenResource(jedis);
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
			Map<byte[], byte[]> map = jedis.hgetAll(RedisSerialize.serialize(sessionKey));
			byte[] keys = RedisSerialize.serialize(key);
			if (map.containsKey(keys)) {
				map.remove(keys);
			}

			map.put(keys, RedisSerialize.serialize(value));

			jedis.hmset(RedisSerialize.serialize(sessionKey), map);
			jedis.expire(RedisSerialize.serialize(sessionKey), timeOut);
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
			jedis.del(RedisSerialize.serialize(sessionKey));
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
}

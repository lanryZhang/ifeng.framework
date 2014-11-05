package com.ifeng.framework.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisClient implements IRedis {
	private String redisName;
	private Jedis jedis;
	private JedisPool jedisPool;
	private boolean getJedisInstance = true;
	
	public RedisClient(String name) {
		this.redisName = name;
		initJedis();
	}

	public void initJedis() {
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
	public <T extends Serializable> List<T> getList(String key) throws Exception {
		try {
			getResource();
			byte[] keys = RedisSerialize.serialize(key);
			List<byte[]> list = jedis.lrange(keys, 0, -1);
			List<T> res = new ArrayList<T>();
			T t = null;
			for (byte[] bs : list) {
				t = RedisSerialize.deserialize(bs);
				res.add(t);
			}
			return res;
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
	public <T extends Serializable> T get(String key) throws Exception {
		try {
			getResource();
			byte[] keys = RedisSerialize.serialize(key);
			byte[] en = jedis.get(keys);
			// 反序列化
			return RedisSerialize.deserialize(en);
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
	public String getString(String key) throws Exception {
		try {
			getResource();
			return jedis.get(key);
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
	public List<String> getStringList(String key) throws Exception {
		try {
			getResource();
			return jedis.lrange(key, 0, -1);
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
	public void setListString(String key, List<String> list) throws Exception {
		try {
			getResource();
			jedis.del(key);
			for (String v : list) {
				jedis.lpush(key, v);
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
	public <T extends Serializable> String set(String key, T t) throws Exception {
		try {
			getResource();
			byte[] keys = RedisSerialize.serialize(key);
			byte[] values = RedisSerialize.serialize(t);
			return jedis.set(keys, values);
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
	public <T extends Serializable> void set(String key, List<T> list) throws Exception {
		try {
			getResource();
			byte[] keys = RedisSerialize.serialize(key);
			jedis.del(keys);
			for (T t : list) {
				byte[] values = RedisSerialize.serialize(t);
				jedis.lpush(keys, values);
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
	public String setString(String key, String value) throws Exception {
		try {
			getResource();
			return jedis.set(key, value);
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
	public <T extends Serializable> void set(String key, List<T> list, int expire) throws Exception {
		set(key, list);
		expireBytesKey(key, expire);
	}

	@Override
	public <T extends Serializable> String set(String key, T t, int expire) throws Exception {
		String res = set(key, t);
		expireBytesKey(key, expire);
		return res;
	}

	@Override
	public String setString(String key, String value, int expire) throws Exception {
		String res = setString(key, value);
		expireStringKey(key, expire);
		return res;
	}

	@Override
	public void setListString(String key, List<String> list, int expire) throws Exception {
		set(key, list);
		expireBytesKey(key, expire);
	}

	@Override
	public void expireStringKey(String key, int expire) throws Exception {
		try {
			getResource();
			jedis.expire(key, expire);
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
	public void expireBytesKey(String key, int expire) throws Exception {
		try {
			getResource();
			byte[] keys = RedisSerialize.serialize(key);
			jedis.expire(keys, expire);
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
	public <T extends Serializable> void lpush(String key, T t) throws Exception {
		try {
			getResource();
			byte[] keys = RedisSerialize.serialize(key);
			byte[] values = RedisSerialize.serialize(t);
			jedis.lpush(keys, values);
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
	public <T extends Serializable> void lpushString(String key, T t) throws Exception {
		try {
			getResource();
			byte[] keys = RedisSerialize.serialize(key);
			byte[] values = RedisSerialize.serialize(t);
			jedis.lpush(keys, values);
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
	public <T extends Serializable> T lpop(String key) throws Exception {
		try {
			getResource();
			byte[] keys = RedisSerialize.serialize(key);
			byte[] res = jedis.lpop(keys);
			return RedisSerialize.deserialize(res);
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
	public <T extends Serializable> T lpopString(String key) throws Exception {
		try {
			getResource();
			byte[] keys = RedisSerialize.serialize(key);
			byte[] res = jedis.lpop(keys);
			return RedisSerialize.deserialize(res);
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
	public long delString(String key) throws Exception {
		try {
			getResource();
			return jedis.del(key);
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
	public <T extends Serializable> long del(T key) throws Exception {
		try {
			getResource();
			byte[] keys = RedisSerialize.serialize(key);
			return jedis.del(keys);
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
	public <T extends Serializable> boolean exists(T key) throws Exception {
		try {
			getResource();
			return jedis.exists(RedisSerialize.serialize(key));
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
	public boolean existsString(String key) throws Exception {
		try {
			getResource();
			return jedis.exists(key);
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
}

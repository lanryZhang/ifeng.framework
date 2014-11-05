package com.ifeng.framework.redis;

import java.io.Serializable;
import java.util.List;

public interface IRedis {
	/**
	 * 获取list对象
	 * 
	 * @param key
	 *            内部转换成byte[]
	 * @return
	 * @throws Exception
	 */
	public <T extends Serializable> List<T> getList(String key) throws Exception;

	/**
	 * 获取存储对象
	 * 
	 * @param key
	 *            内部转换成byte[]
	 * @return
	 * @throws Exception
	 */
	public <T extends Serializable> T get(String key) throws Exception;

	/**
	 * 获取存储的值 --字符串类型
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String getString(String key) throws Exception;

	/**
	 * 获取List<String>
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public List<String> getStringList(String key) throws Exception;

	/**
	 * 存储list对象
	 * 
	 * @param key
	 *            内部转换成byte[]
	 * @param list
	 * @throws Exception
	 */
	public <T extends Serializable> void set(String key, List<T> list) throws Exception;

	/**
	 * 存储list对象，并且设置过期时间
	 * 
	 * @param key
	 *            内部转换成byte[]
	 * @param list
	 * @param expire
	 *            秒
	 * @throws Exception
	 */
	public <T extends Serializable> void set(String key, List<T> list, int expire) throws Exception;

	/**
	 * 存储单个实例对象
	 * 
	 * @param key
	 *            内部转换成byte[]
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public <T extends Serializable> String set(String key, T t) throws Exception;

	/**
	 * 存储单个实例对象，并且设置过期时间
	 * 
	 * @param key
	 *            内部转换成byte[]
	 * @param t
	 * @param expire
	 *            秒
	 * @return
	 * @throws Exception
	 */
	public <T extends Serializable> String set(String key, T t, int expire) throws Exception;

	/**
	 * 存储string类型的值
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public String setString(String key, String value) throws Exception;

	/**
	 * 存储string类型的值，并且设置过期时间
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 *            秒
	 * @return
	 * @throws Exception
	 */
	public String setString(String key, String value, int expire) throws Exception;

	/**
	 * 存储List<String>对象
	 * 
	 * @param key
	 * @param list
	 * @throws Exception
	 */
	public void setListString(String key, List<String> list) throws Exception;

	/**
	 * 存储List<String>对象,并且设置过期时间
	 * 
	 * @param key
	 * @param list
	 * @param expire
	 *            秒
	 * @throws Exception
	 */
	public void setListString(String key, List<String> list, int expire) throws Exception;

	/**
	 * List中插入一条数据
	 * 
	 * @param key
	 *            内部转换成byte[]
	 * @param t
	 * @throws Exception
	 */
	public <T extends Serializable> void lpush(String key, T t) throws Exception;

	/**
	 * 弹出一条数据
	 * 
	 * @param key
	 *            内部转换成byte[]
	 * @return
	 * @throws Exception
	 */
	public <T extends Serializable> T lpop(String key) throws Exception;

	/**
	 * List中插入一条string类型数据
	 * 
	 * @param key
	 * @param t
	 * @throws Exception
	 */
	public <T extends Serializable> void lpushString(String key, T t) throws Exception;

	/**
	 * List中弹出一条string类型数据
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public <T extends Serializable> T lpopString(String key) throws Exception;

	/**
	 * 删除一个String类型的键值对
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public long delString(String key) throws Exception;

	/**
	 * 删除一个非String类型的键值对
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public <T extends Serializable> long del(T key) throws Exception;

	/**
	 * 判断是否存在一个Key
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public <T extends Serializable> boolean exists(T key) throws Exception;

	/**
	 * 判断是否存在一个String类型的key
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public boolean existsString(String key) throws Exception;

	/**
	 * 设置缓存过期时间
	 * 
	 * @param key
	 * @param expire
	 * @throws Exception
	 */
	public void expireStringKey(String key, int expire) throws Exception;

	/**
	 * 设置缓存过期时间
	 * 
	 * @param key
	 * @param expire
	 * @throws Exception
	 */
	public void expireBytesKey(String key, int expire) throws Exception;

	/**
	 * 回收Jedis对象到连接池
	 */
	public void returnResource();

	/**
	 * 回收Jedis对象到连接池
	 */
	void returnBrokenResource();
}

package com.ifeng.framework.mongo;

public class MongoFactory {

	private static MongoCli instance;

	/**
	 * 默认数据库
	 * @return
	 */
	public static MongoCli getInstance()  {
		if (instance == null){
			synchronized (MongoFactory.class) {
				if (instance == null){
					instance = new MongoCli();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 初始化Mongo实例，并且切换到指定数据库
	 * @param dbname
	 * @return
	 */
	public static MongoCli getInstance(String dbname){
		if (instance == null){
			synchronized (MongoFactory.class) {
				if (instance == null){
					instance = new MongoCli();
					instance.changeDb(dbname);
				}
			}
		}
		return instance;
	}
	
	/**
	 * 初始化Mongo实例，切换到指定数据库并且做登陆验证
	 * @param dbname
	 * @return
	 */
	public static MongoCli getInstance(String dbname,
			String username,String password){
		if (instance == null){
			synchronized (MongoFactory.class) {
				if (instance == null){
					instance = new MongoCli();
					instance.changeDb(dbname);
					boolean res = instance.authUser(username, password);
					if (!res){
						try {
							instance.close();
							instance = null;
						} catch (Exception e) {
						}
					}
				}
			}
		}
		return instance;
	}
	
}

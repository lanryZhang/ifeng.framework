package com.ifeng.framework.mongo;

public class MongoFactory {

	private static MongoCli instance;

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

}

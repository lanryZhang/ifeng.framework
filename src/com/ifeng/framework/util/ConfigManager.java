package com.ifeng.framework.util;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

public class ConfigManager {
	private static XStream instance;
	private static Map<String, Object> settings;
	private static Map<String, Object> cacheKeySettings;
	private static Map<String,RedisConfig> redisConfigs;
	private static List<MongoConfig> mongoSettings;
	
	private static XStream getInstance() {
		if (instance == null){
			synchronized (ConfigManager.class) {
				if (instance == null){
					instance = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
				}
			}
		}
		return instance;
	}
	
	public  static<T> String toXml(T t) {
		return getInstance().toXML(t);
	}
	
	@SuppressWarnings("unchecked")
	public  static<T> T toObject(String xml) {
		try {
			return  (T) getInstance().fromXML(xml);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Map<String, Object> getSettings() {
		if (settings == null){
			synchronized (ConfigManager.class) {
				if (settings == null){
					ConfigSettings.intSetting();
				}
			}
		}
		return settings;
	}

	public static Map<String,RedisConfig> getRedisConfigs() {
		if (redisConfigs == null){
			synchronized (ConfigManager.class) {
				if (redisConfigs == null){
					redisConfig.initRedis();
				}
			}
		}
		return redisConfigs;
	}
	public static Map<String,Object> getCacheKeySettings() {
		if (cacheKeySettings == null){
			synchronized (ConfigManager.class) {
				if (cacheKeySettings == null){
					CacheSettings.intSetting();
				}
			}
		}
		return cacheKeySettings;
	}


	public static List<MongoConfig> getMongoSettings() {
		if (mongoSettings == null){
			synchronized (ConfigManager.class) {
				if (mongoSettings == null){
					mongoConfig.initMongo();
				}
			}
		}
		return mongoSettings;
	}


	static class CacheSettings {
		public static void intSetting() {
			try {
				String path= ConfigManager.class.getClassLoader().getResource("").getPath();
				path =path+"cacheSettings.xml"; 
				path = URLDecoder.decode(path, "UTF-8"); 
				getInstance().alias("settings", List.class);
				getInstance().alias("add", SettingItem.class);
				getInstance().useAttributeFor(SettingItem.class, "key");
				getInstance().useAttributeFor(SettingItem.class, "value");
				
				File file = new File(path);
				if (cacheKeySettings == null){
					cacheKeySettings = new HashMap<String, Object>();
				}
				
				@SuppressWarnings("unchecked")
				List<SettingItem> list= (List<SettingItem>) getInstance().fromXML(file);
				
				for (SettingItem item : list) {
					if (!cacheKeySettings.containsKey(item.getKey())){
						cacheKeySettings.put(item.getKey(), item.getValue());	
					}
				}
				
			} catch (Exception e) {
				LogUtil.error(e);
			}
		}
	}
	
	static class ConfigSettings {
		public static void intSetting() {
			try {
				String path= ConfigManager.class.getClassLoader().getResource("").getPath();
				path =path+"settings.xml"; 
				path = URLDecoder.decode(path, "UTF-8"); 
				getInstance().alias("settings", List.class);
				getInstance().alias("add", SettingItem.class);
				getInstance().useAttributeFor(SettingItem.class, "key");
				getInstance().useAttributeFor(SettingItem.class, "value");
				
				File file = new File(path);
				if (settings == null){
					settings = new HashMap<String, Object>();
				}
				
				@SuppressWarnings("unchecked")
				List<SettingItem> list= (List<SettingItem>) getInstance().fromXML(file);
				
				for (SettingItem item : list) {
					if (!settings.containsKey(item.getKey())){
						settings.put(item.getKey(), item.getValue());	
					}
				}
				
			} catch (Exception e) {
				LogUtil.error(e);
			}
		}
	}
	
	static class redisConfig{
		public static void initRedis() {
			try {
				String path= ConfigManager.class.getClassLoader().getResource("").getPath();
				path =path+"redis.xml";
				path = URLDecoder.decode(path, "UTF-8"); 
				getInstance().alias("redis", List.class);
				getInstance().alias("instance", RedisConfig.class);
				File file = new File(path);
				if (redisConfigs == null){
					redisConfigs = new HashMap<String, RedisConfig>();
				}
				@SuppressWarnings("unchecked")
				List<RedisConfig> list= (List<RedisConfig>) getInstance().fromXML(file);
				
				for (RedisConfig item : list) {
					if (!redisConfigs.containsKey(item.getName())){
						redisConfigs.put(item.getName(), item);	
					}
				}
			} catch (Exception e) {
				LogUtil.error(e);
			}
		}
	}
	
	static class mongoConfig{
		@SuppressWarnings("unchecked")
		public static void initMongo() {
			try {
				String path= ConfigManager.class.getClassLoader().getResource("").getPath();
				path =path+"mongo.xml";
				getInstance().alias("mongo", List.class);
				getInstance().alias("instance", MongoConfig.class);
				File file = new File(path);

				mongoSettings = (List<MongoConfig>) getInstance().fromXML(file);

			} catch (Exception e) {
				LogUtil.error(e);
			}
		}
	}
}

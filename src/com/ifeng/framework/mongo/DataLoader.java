package com.ifeng.framework.mongo;

import com.ifeng.framework.util.LogUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DataLoader {
	private DBObject dbObject;
	
	public DataLoader(DBObject obj) {
		this.dbObject = obj;
	}
	
	public DataLoader(DBObject obj,boolean isMapReduceResult) {
		if (isMapReduceResult){
			dbObject = new BasicDBObject();
			DBObject _id = (DBObject)obj.get("_id");
			for (String item : _id.keySet()) {
				dbObject.put(item, _id.get(item));
			}
			
			DBObject _values = (DBObject)obj.get("value");
			for (String item : _values.keySet()) {
				dbObject.put(item, _values.get(item));
			}
		}
		else {
			this.dbObject = obj;
		}
	}
	
	public int getInt(String key,int defaultValue) {
		if (dbObject.containsField(key)){
			return (int)Double.parseDouble(dbObject.get(key).toString());
		}
		return defaultValue;
	}
	
	public int getInt(String key) {
		return getInt(key,0);
	}
	
	public String getString(String key) {
		if (dbObject.containsField(key)){
			return dbObject.get(key).toString();
		}
		return "";
	}
	
	public DataLoader getLoader(String key) {
		try {
			if (dbObject.containsField(key)){
				return new DataLoader((DBObject)dbObject.get(key));
			}
		} catch (Exception e) {
			LogUtil.error(e);
		}
		
		return null;
	}
}

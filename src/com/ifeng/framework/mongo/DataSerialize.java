package com.ifeng.framework.mongo;

import java.lang.reflect.Field;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DataSerialize {
	/**
	 * 序列化实例
	 * @param en
	 * @return
	 */
	public DBObject Secialize(){
		return Secialize(this);
	}
	
	private <T> DBObject Secialize(T t){
		DBObject en = new BasicDBObject();
		try {
			Field[] fields = t.getClass().getDeclaredFields();
			Field.setAccessible(fields, true);
			if (fields != null){
				for (Field field : fields) {
					
					if (DataSerialize.class.isAssignableFrom(field.getType())
							&& IDataRow.class.isAssignableFrom(field.getType())){
						DBObject res = Secialize(field.get(t));
						en.put(field.getName(), res);
					}
					else {
						en.put(field.getName(), field.get(t));
					}
					
					
				}
			}
		} catch (Exception e) {
		}
		
		return en;
	}
	
}
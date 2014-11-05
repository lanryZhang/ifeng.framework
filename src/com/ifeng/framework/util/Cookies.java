package com.ifeng.framework.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

class Cookies {
	private HashMap<String, String> cookiesHashMap;
	
	public Cookies() {
		cookiesHashMap = new HashMap<String, String>();
	}
	
	public static final Cookies getEmptyCookieObj() {
		return new Cookies();
	}
	
	public void clear() {
		this.cookiesHashMap.clear();
	}
	
	public String getCookie(String key) {
		return cookiesHashMap.get(key);
	}
	
	public String putCookie(String key,String value) {
		return cookiesHashMap.put(key, value);
	}
	
	@Override
	public String toString() {
		StringBuffer sbBuffer = new StringBuffer();
		Iterator<Entry<String, String>> iterator = cookiesHashMap.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, String> entry = iterator.next();
			sbBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
		}
		if (sbBuffer.length() > 0){
			return sbBuffer.substring(0, sbBuffer.length() - 1);
		}
		return "";
	}
}

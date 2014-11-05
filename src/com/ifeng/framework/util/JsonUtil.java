package com.ifeng.framework.util;

import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtil {
	private static ObjectMapper objectMapper;

	public static ObjectMapper getInstance() {
		if (objectMapper == null){
			objectMapper = new ObjectMapper();
		}
		return objectMapper;
	}
}

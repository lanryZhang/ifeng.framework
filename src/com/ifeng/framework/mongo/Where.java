package com.ifeng.framework.mongo;

import java.util.ArrayList;
import java.util.List;


public class Where {
	private List<WhereItem> list = new ArrayList<WhereItem>();

	public Where() {
	}

	public Where(String name, Object value) {
		list.add(new WhereItem(name, value));
	}

	public Where(String name, WhereType whereType, Object value) {
		list.add(new WhereItem(name, whereType, value));
	}

	public Where Add(String name, Object value) {
		list.add(new WhereItem(name, value));
		return this;
	}

	public Where Add(String name, WhereType whereType, Object value) {
		list.add(new WhereItem(name, whereType, value));
		return this;
	}
	
	public List<WhereItem> getList() {
		return list;
	}
}

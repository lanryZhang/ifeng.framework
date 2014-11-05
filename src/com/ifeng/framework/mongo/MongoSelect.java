package com.ifeng.framework.mongo;

import java.util.ArrayList;
import java.util.List;

public class MongoSelect {
	private List<SelectField> fields = null;
	private Where condition = new Where();
	private int pageIndex;
	private int pageSize;
	private List<String> groupBy;
	private List<OrderBy> orderBy;
	
	public MongoSelect(){
		this.fields = new ArrayList<SelectField>();
		pageIndex = 1;
		pageSize = 25;
		this.groupBy  = new ArrayList<String>();
		this.orderBy = new ArrayList<OrderBy>();
		condition = new Where();
	}
	
	public MongoSelect(List<SelectField> fields){
		this.fields =fields; 
		pageIndex = 1;
		pageSize = 25;
		this.groupBy  = new ArrayList<String>();
		this.orderBy = new ArrayList<OrderBy>();
		condition = new Where();
	}
	
	public MongoSelect(List<SelectField> fields,int pageIndex,int pageSize){
		this.fields =fields; 
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.groupBy  = new ArrayList<String>();
		this.orderBy = new ArrayList<OrderBy>();
		condition = new Where();
	}
	
	/**
	 * 增加筛选条件 按照等于处理
	 * @param name
	 * @param value
	 * @return
	 */
	public MongoSelect where(String name, Object value)
    {
       	where(name, WhereType.Equal, value);
       	return this;
    }
	
	/**
	 * 增加筛选条件
	 * @param name 字段名称
	 * @param whereType 条件
	 * @param value 值
	 * @return
	 */
	public MongoSelect where(String name, WhereType whereType, Object value)
    {
        this.condition.getList().add(new WhereItem(name, whereType, value));
        return this;
    }
	
	/**
	 * 增加分组字段
	 * @param field
	 * @return
	 */
	public MongoSelect groupBy(String field)
    {
		groupBy.add(field);
        return this;
    }

	/**
	 * 添加排序字段
	 * @param orderBy 排序实例
	 * @return
	 */
	public MongoSelect orderBy(OrderBy orderBy)
    {
		this.orderBy.add(orderBy);
        return this;
    }
	
	/**
	 * 添加查询字段
	 * @param field 字段名称
	 * @return
	 */
	public MongoSelect addField(String field) {
		this.fields.add(new SelectField(field));
		return this;
	}
	/**
	 * 添加查询字段
	 * @param field 字段名称
	 * @param alias 字段别名
	 * @return
	 */
	public MongoSelect addField(String field,String alias) {
		this.fields.add(new SelectField(field,alias));
		return this;
	}
	
	public int getPageIndex() {
		return pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public List<String> getGroupBy() {
		return groupBy;
	}

	public List<OrderBy> getOrderBy() {
		return orderBy;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setGroupBy(List<String> groupBy) {
		this.groupBy = groupBy;
	}


	public List<SelectField> getFields() {
		return fields;
	}

	public Where getCondition() {
		return condition;
	}
}

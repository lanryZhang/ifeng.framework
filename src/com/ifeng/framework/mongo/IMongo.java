package com.ifeng.framework.mongo;

import java.util.List;
import java.util.Map;

import com.mongodb.MapReduceCommand.OutputType;
import com.mongodb.MapReduceOutput;
import com.mongodb.WriteResult;


public interface IMongo {
	/**
	 * 根据condition的值，获取集合中符合条件的一条文档
	 * @param condition 筛选条件
	 * @param classType 实体类型
	 * @return
	 * @throws Exception
	 */
	public <T extends IDataRow> T selectOne(MongoSelect select,Class<T> classType) throws Exception;

	/**
	 * 获取集合内所有文档
	 * @param classType 实体类型
	 * @param select 
	 * @return
	 * @throws Exception
	 */
	public <T extends IDataRow> List<T> selectAll(MongoSelect select, Class<T> classType) throws Exception;
	
	/**
	 * 根据condition的值，获取集合中符合条件的所有文档
	 * @param select 筛选条件
	 * @param classType 实体类型
	 * @return
	 * @throws Exception
	 */
	public <T extends IDataRow> List<T> selectList(MongoSelect select, Class<T> classType) throws Exception;
	
	/**
	 * 删除符合条件的文档
	 * @param where 筛选条件
	 * @return
	 * @throws Exception
	 */
	public WriteResult remove(Where where) throws Exception;
	
	/**
	 * 保存一个文档到数据库
	 * @param en 实例
	 * @return
	 * @throws Exception
	 */
	public <T  extends DataSerialize> WriteResult insert(T en)  throws Exception;
	
	/**
	 * 保存一组文档到数据库
	 * @param list 实例列表
	 * @return
	 * @throws Exception
	 */
	public <T  extends DataSerialize> WriteResult insert(List<T> list)  throws Exception;

	/**
	 * 执行MapReduce
	 * @param map 
	 * @param reduce
	 * @param outputTarget
	 * @param outputType
	 * @param where
	 * @return
	 */
	public MapReduceOutput mapReduce(String map, String reduce, String outputTarget, OutputType outputType, Where where);

	/**
	 * 完全更新符合条件的文档，只保留Map中的字段，其余字段删除
	 * @param select
	 * @return
	 * @throws Exception
	 */
	public WriteResult update(Map<String, Object> fields, Where where) throws Exception;

	/**
	 * 更新符合条件的文档，只更新map中的字段，其余不变
	 * @param select
	 * @return
	 * @throws Exception
	 */
	public WriteResult updateE(Map<String, Object> fields, Where where) throws Exception;

}

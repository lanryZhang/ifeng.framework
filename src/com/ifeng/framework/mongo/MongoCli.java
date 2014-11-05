package com.ifeng.framework.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ifeng.framework.util.ConfigManager;
import com.ifeng.framework.util.LogUtil;
import com.ifeng.framework.util.MongoConfig;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceCommand.OutputType;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

public class MongoCli implements IMongo {

	private MongoClient mongoClient;
	private DB db;
	private DBCollection collection;

	public MongoCli() {
		initMongo();
	}

	private void initMongo() {
		try {
			List<MongoConfig> configs = ConfigManager.getMongoSettings();
			ServerAddress serverAddress = null;
			if (configs.size() <= 0) {
				LogUtil.error("配置文件不完整");
			}
			if (configs.size() == 1) {
				serverAddress = new ServerAddress(configs.get(0).getIp(), configs.get(0).getPort());
				mongoClient = new MongoClient(serverAddress);
			} else {
				List<ServerAddress> serverAddresses = new ArrayList<ServerAddress>();
				for (MongoConfig config : configs) {
					serverAddress = new ServerAddress(config.getIp(), config.getPort());
					serverAddresses.add(serverAddress);
				}

				mongoClient = new MongoClient(serverAddresses);
			}
		} catch (Exception e) {
			LogUtil.error(e);
		}

	}

	/**
	 * 切换数据库
	 * 
	 * @param dbname
	 *            数据库名称
	 */
	public void changeDb(String dbname) {
		if (mongoClient == null) {
			throw new NullPointerException();
		}
		db = mongoClient.getDB(dbname);
	}

	/**
	 * 获取集合
	 * 
	 * @param name
	 *            集合名称
	 */
	public void getCollection(String name) {
		if (db == null) {
			throw new NullPointerException();
		}
		collection = db.getCollection(name);
	}

	private DBObject createSort(MongoSelect select) {
		if (select.getOrderBy() != null && select.getOrderBy().size() > 0) {
			DBObject sort = new BasicDBObject();
			for (OrderBy item : select.getOrderBy()) {
				sort.put(item.name, item.direction.value());
			}
			return sort;
		}
		return null;
	}

	private DBObject createSelectFields(MongoSelect select) {
		DBObject fields = new BasicDBObject();
		if (select != null && select.getFields() != null) {
			for (SelectField item : select.getFields()) {
				fields.put(item.getAlias(), true);
			}
		}
		return fields;
	}

	private DBObject createUpdateFields(Map<String, Object> fields) {
		DBObject result = new BasicDBObject();
		if (fields != null) {
			for (Entry<String, Object> item : fields.entrySet()) {
				result.put(item.getKey(), item.getValue());
			}
		}
		return result;
	}

	private DBObject createCondition(Where where) {
		DBObject condition = new BasicDBObject();
		if (where.getList() != null) {
			for (WhereItem item : where.getList()) {
				if (item.getWhereType() == WhereType.Equal) {
					condition.put(item.getName(), item.getValue());
				} else {
					condition.put(item.getName(), new BasicDBObject(item.getWhereType().value(), item.getValue()));
				}
			}
		}
		return condition;
	}

	private Map<String, String> createMapReduce(MongoSelect select) throws Exception {
		if (select.getGroupBy() != null && select.getGroupBy().size() > 0) {
			StringBuilder values = new StringBuilder();
			StringBuilder recudeVar = new StringBuilder();
			List<String> vars = new ArrayList<String>();
			boolean hasAvgMethod = false;
			for (SelectField item : select.getFields()) {
				String fn = item.getAlias().trim();
				String name = item.getName().trim().toLowerCase();
				if (name.contains("sum(")) {
					values.append(fn).append(":this.").append(item.getName().replace("sum(", "").replace(")", "")).append(",");
					recudeVar.append("var ").append(fn).append("=0;");
					if (!vars.contains(fn)) {
						vars.add(fn);
					} else {
						throw new Exception("查询字段名不能重复.");
					}
				}
				if (name.contains("count(")) {
					values.append(fn).append(":1,");
					recudeVar.append("var ").append(fn).append("=0;");
					if (!vars.contains(fn)) {
						vars.add(fn);
					} else {
						throw new Exception("查询字段名不能重复.");
					}
				}
				if (name.contains("max(")) {
					values.append(fn).append(":Math.max(this.").append(item.getName().replace("max(", "")).append(",");
					recudeVar.append("var ").append(fn).append("=0;");
					if (!vars.contains(fn)) {
						vars.add(fn);
					} else {
						throw new Exception("查询字段名不能重复.");
					}
				}
				if (name.contains("min(")) {
					values.append(fn).append(":Math.min(this.").append(item.getName().replace("min(", "")).append(",");
					recudeVar.append("var ").append(fn).append("=0;");
					if (!vars.contains(fn)) {
						vars.add(fn);
					} else {
						throw new Exception("查询字段名不能重复.");
					}
				}
				if (name.contains("avg(")) {
					hasAvgMethod = true;
					values.append(fn).append(":this.").append(item.getName().replace("avg(", "").replace(")", "")).append(",");
					recudeVar.append("var ").append(fn).append("=0;");
					if (!vars.contains(fn)) {
						vars.add(fn);
					} else {
						throw new Exception("查询字段名不能重复.");
					}
				}
			}
			if (values.length() <= 0) {
				return null;
			}
			values = values.deleteCharAt(values.length() - 1);
			StringBuilder groupKeys = new StringBuilder();
			for (String item : select.getGroupBy()) {
				groupKeys.append(item).append(":").append("this.").append(item).append(",");
			}
			groupKeys = groupKeys.deleteCharAt(groupKeys.length() - 1);

			StringBuilder mapCode = new StringBuilder();
			mapCode.append("function(){emit({").append(groupKeys).append("},{").append(values).append("});}");

			StringBuilder finalizeFunc = new StringBuilder();
			StringBuilder reduceCode = new StringBuilder();
			StringBuilder returnObj = new StringBuilder("return {");

			reduceCode.append("function(key,values){");
			reduceCode.append(recudeVar);
			reduceCode.append("for(var i = 0; i < values.length;i++){");

			for (String var : vars) {
				reduceCode.append(var).append(" += values[i].").append(var).append(";");
				returnObj.append(var).append(":").append(var).append(",");
			}
			if (hasAvgMethod) {
				finalizeFunc.append("function (key, reducedValue) {").append("reducedValue.Avg = reducedValue.Sum/reducedValue.Count;")
						.append("return reducedValue;}");
			}

			returnObj = returnObj.length() > 8 ? returnObj.deleteCharAt(returnObj.length() - 1).append("}") : new StringBuilder("");
			reduceCode.append("} ").append(returnObj).append(";}");
			Map<String, String> result = new HashMap<String, String>();
			result.put("map", mapCode.toString());
			result.put("reduce", reduceCode.toString());
			return result;
		}
		return null;
	}

	@Override
	public <T extends IDataRow> List<T> selectAll(MongoSelect select, Class<T> classType) throws Exception {

		DBCursor cursor = collection.find(null, createSelectFields(select));

		if (select.getOrderBy() != null) {
			DBObject orderBy = createSort(select);
			cursor.sort(orderBy);
		}
		ArrayList<T> list = new ArrayList<T>();
		T en = null;
		while (cursor.hasNext()) {
			en = classType.newInstance();
			DataLoader loader = new DataLoader(cursor.next());
			en.LoadData(loader);
			list.add(en);
		}
		return list;
	}

	@Override
	public <T extends IDataRow> T selectOne(MongoSelect select, Class<T> classType) throws Exception {
		T en = classType.newInstance();
		DBObject condition = createCondition(select.getCondition());
		DBObject fields = createSelectFields(select);
		DBObject object = collection.findOne(condition, fields);
		if (object == null)
			return null;

		DataLoader loader = new DataLoader(object);
		en.LoadData(loader);

		return en;
	}

	@Override
	public <T extends IDataRow> List<T> selectList(MongoSelect select, Class<T> classType) throws Exception {
		DBCursor cursor = null;
		DBObject fields = createSelectFields(select);
		DBObject where = createCondition(select.getCondition());
		boolean isMapReduce = false;
		if (select != null) {

			if (select.getGroupBy() != null && select.getGroupBy().size() > 0) {
				Map<String, String> mapReduce = createMapReduce(select);
				MapReduceOutput output = collection.mapReduce(mapReduce.get("map"), mapReduce.get("reduce"), "reduceCollection", OutputType.REPLACE,
						where);
				cursor = output.getOutputCollection().find();
				isMapReduce = true;
			} else {
				cursor = collection.find(where, fields);
			}

			if (select.getOrderBy() != null) {
				DBObject orderBy = createSort(select);
				cursor.sort(orderBy);
			}
		}

		ArrayList<T> list = new ArrayList<T>();
		T en = null;
		while (cursor.hasNext()) {
			en = classType.newInstance();
			DataLoader loader = new DataLoader(cursor.next(), isMapReduce);
			en.LoadData(loader);
			list.add(en);
		}
		return list;
	}

	@Override
	public WriteResult remove(Where where) throws Exception {
		DBObject condition = createCondition(where);
		return collection.remove(condition, WriteConcern.SAFE);
	}

	@Override
	public WriteResult updateE(Map<String, Object> fields, Where where) throws Exception {
		DBObject condition = createCondition(where);
		DBObject updates = createUpdateFields(fields);

		return collection.update(condition, updates);
	}

	@Override
	public WriteResult update(Map<String, Object> fields, Where where) throws Exception {
		DBObject condition = createCondition(where);
		DBObject en = collection.findOne(condition);
		for (Entry<String, Object> itemEntry : fields.entrySet()) {
			en.removeField(itemEntry.getKey());
			en.put(itemEntry.getKey(), itemEntry.getValue());
		}
		return collection.update(condition, en);
	}

	@Override
	public <T extends DataSerialize> WriteResult insert(T en) throws Exception {
		DBObject dbObject = en.Secialize();
		return collection.insert(dbObject);
	}

	@Override
	public <T extends DataSerialize> WriteResult insert(List<T> list) throws Exception {
		List<DBObject> dbObjects = new ArrayList<DBObject>();
		for (T t : list) {
			dbObjects.add(t.Secialize());
		}
		return collection.insert(dbObjects);
	}

	@Override
	public MapReduceOutput mapReduce(String map, String reduce, String outputTarget, MapReduceCommand.OutputType outputType, Where where) {
		DBObject cond = createCondition(where);
		return collection.mapReduce(map, reduce, outputTarget, outputType, cond);
	}
	// ===========Setter Getter======================
}

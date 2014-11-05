package com.ifeng.framework.redis;


public class RedisFactory {
	private static ShardedRedisClient shardedRedisClient;
	private static RedisClient redisClient;
	private static ShardSessionEx shardSessionEx;
	private static SessionEx sessionEx;
	
	public static ShardedRedisClient getShardedRedisClient() {
		if (shardedRedisClient == null){
			synchronized (RedisFactory.class) {
				if (shardedRedisClient == null){
					shardedRedisClient = new ShardedRedisClient("up");
				}
			}
		}
		return shardedRedisClient;
	}
	public static RedisClient getRedisClient() {
		if (redisClient == null){
			synchronized (RedisFactory.class) {
				if (redisClient == null){
					redisClient = new RedisClient("redis_1");	
				}
			}
		}
		return redisClient;
	}
	
	public static ShardSessionEx getShardSession() {
		if (shardSessionEx == null){
			synchronized (RedisFactory.class) {
				if (shardSessionEx == null){
					shardSessionEx = new ShardSessionEx("up");
				}
			}
		}
		return shardSessionEx;
	}
	
	public static SessionEx getSession() {
		if (sessionEx == null){
			synchronized (RedisFactory.class) {
				if (sessionEx == null){
					sessionEx = new SessionEx("redis_1");
				}
			}
		}
		return sessionEx;
	}
}

package com.ifeng.framework.util;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("instance")
public class RedisConfig {
	@XStreamAlias("name")
	private String name;
	@XStreamAlias("maxActive")
	private int maxActive;
	@XStreamAlias("maxIdle")
	private int maxIdle;
	@XStreamAlias("maxWait")
	private int maxWait;
	@XStreamAlias("testOnBorrow")
	private boolean testOnBorrow;
	@XStreamAlias("serverIp")
	private String serverIp;
	@XStreamAlias("port")
	private int port;
	@XStreamAlias("shard")
	private String shard;
	@XStreamAlias("description")
	private String description;
	@XStreamAlias("testOnReturn")
	private boolean testOnReturn;
	public int getMaxActive() {
		return maxActive;
	}
	
	public int getMaxIdle() {
		return maxIdle;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public String getServerIp() {
		return serverIp;
	}

	public int getPort() {
		return port;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShard() {
		return shard;
	}

	public void setShard(String shard) {
		this.shard = shard;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	
}

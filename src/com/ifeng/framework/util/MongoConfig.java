package com.ifeng.framework.util;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("instance")
public class MongoConfig {
	@XStreamAlias("name")
	private String name;
	@XStreamAlias("ip")
	private String ip;
	@XStreamAlias("port")
	private int port;
	
	public String getName() {
		return name;
	}
	public String getIp() {
		return ip;
	}
	public int getPort() {
		return port;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}

package com.example.demo.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.cloud.consul.http")
public class ConsulHttpProperties {
	
	private int maxConnections = 1000;
	private int maxPerRouteConnections = 500;
	private int connectionTimeout = 10000; // 10 sec
	// 10 minutes for read timeout due to blocking queries timeout
	private int readTimeout = 60000 * 10; // 10 min
	public int getMaxConnections() {
		return maxConnections;
	}
	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}
	public int getMaxPerRouteConnections() {
		return maxPerRouteConnections;
	}
	public void setMaxPerRouteConnections(int maxPerRouteConnections) {
		this.maxPerRouteConnections = maxPerRouteConnections;
	}
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public int getReadTimeout() {
		return readTimeout;
	}
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
	@Override
	public String toString() {
		return "ConsulHttpProperties{" + "maxConnections='" + this.maxConnections + '\'' + ", maxPerRouteConnections=" + this.maxPerRouteConnections
				+ ", connectionTimeout=" + this.connectionTimeout + ", readTimeout=" + this.readTimeout + '}';
	}

}

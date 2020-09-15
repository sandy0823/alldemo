package com.example.demo.test.model;

import com.example.demo.test.annotation.ConfigKey;

/**
 * 并发的一些参数
 * 
 * @author sandy
 *
 */
public class Config {
	@ConfigKey("service.start.path")
	private String serviceStartPath;
	@ConfigKey("parallel.threads.count")
	private int parallelThreadsCount = 10;
	@ConfigKey("http.connect.pool.size")
	private int httpConnectPoolSize = 5;
	@ConfigKey("http.connect.timeout")
	private int httpConnectTimeout = 1000;
	@ConfigKey("http.url")
	private String httpURL;
	@ConfigKey("service.restart")
	private boolean serviceRestart = true;
	@ConfigKey("service.jar.name")
	private String serviceJarName;
	@ConfigKey("loop.counts")
	private int loopCounts = 5;

	public String getServiceStartPath() {
		return serviceStartPath;
	}

	public void setServiceStartPath(String serviceStartPath) {
		this.serviceStartPath = serviceStartPath;
	}

	public int getParallelThreadsCount() {
		return parallelThreadsCount;
	}

	public void setParallelThreadsCount(int parallelThreadsCount) {
		this.parallelThreadsCount = parallelThreadsCount;
	}

	public int getHttpConnectPoolSize() {
		return httpConnectPoolSize;
	}

	public void setHttpConnectPoolSize(int httpConnectPoolSize) {
		this.httpConnectPoolSize = httpConnectPoolSize;
	}

	public int getHttpConnectTimeout() {
		return httpConnectTimeout;
	}

	public void setHttpConnectTimeout(int httpConnectTimeout) {
		this.httpConnectTimeout = httpConnectTimeout;
	}

	public String getHttpURL() {
		return httpURL;
	}

	public void setHttpURL(String httpURL) {
		this.httpURL = httpURL;
	}

	public boolean isServiceRestart() {
		return serviceRestart;
	}

	public void setServiceRestart(boolean serviceRestart) {
		this.serviceRestart = serviceRestart;
	}

	public String getServiceJarName() {
		return serviceJarName;
	}

	public void setServiceJarName(String serviceJarName) {
		this.serviceJarName = serviceJarName;
	}

	public int getLoopCounts() {
		return loopCounts;
	}

	public void setLoopCounts(int loopCounts) {
		this.loopCounts = loopCounts;
	}

	@Override
	public String toString() {
		return "Config [serviceStartPath=" + serviceStartPath + ", parallelThreadsCount=" + parallelThreadsCount
				+ ", httpConnectPoolSize=" + httpConnectPoolSize + ", httpConnectTimeout=" + httpConnectTimeout
				+ ", httpURL=" + httpURL + ", serviceRestart=" + serviceRestart + ", serviceJarName=" + serviceJarName
				+ ", loopCounts=" + loopCounts + "]";
	}
}

package com.example.demo.test.model;

public class Result {
    private int threadCount;
    private int successCount;
    private int errorCount;
    private long takeTime;
    
	public Result(int threadCount, int successCount, int errorCount,long takeTime) {
		super();
		this.threadCount = threadCount;
		this.successCount = successCount;
		this.errorCount = errorCount;
		this.takeTime = takeTime;
	}
	public int getThreadCount() {
		return threadCount;
	}
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	public int getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	public int getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	public long getTakeTime() {
		return takeTime;
	}
	public void setTakeTime(long takeTime) {
		this.takeTime = takeTime;
	}
}

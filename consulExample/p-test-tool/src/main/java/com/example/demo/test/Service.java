package com.example.demo.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Service {
	private static AtomicInteger sucessCount = new AtomicInteger(0);
	private static AtomicInteger errorCount = new AtomicInteger(0);
	private static AtomicInteger totalCount = new AtomicInteger(0);
	
	private int threadCounts;
	private String startAPPAbPath;
	private int loopCounts;
	private String resultFilePath;
	private String consulPoolConnections;
	private int connectTimeout;
	
	public Service(int threadCounts,String consulPoolConnections,String startAPPAbPath,int loopCount,
			String resultFilePath,int connectTimeout){
		this.threadCounts = threadCounts;
		this.startAPPAbPath = startAPPAbPath;
		this.loopCounts = loopCount;
		this.resultFilePath = resultFilePath;
		this.consulPoolConnections = consulPoolConnections;
		this.connectTimeout = connectTimeout;
	}
	
	public void loopExec(){
		List<Result> results = Lists.newArrayList();
		for(int i =0;i<loopCounts;i++){
			totalCount.getAndSet(0);
			sucessCount.getAndSet(0);
			errorCount.getAndSet(0);
			results.add(exec());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				//igore
			}
		}
		
		try {
			FileUtils.writeResult(results, resultFilePath, threadCounts,
					consulPoolConnections,connectTimeout);
		} catch (IOException e) {
			log.error("write file error.",resultFilePath);
		}
	}
	
	public Result exec(){
		// 启动程序
		long taketime = 0;
		if (CmdUtils.startApp(startAPPAbPath)) {
			try {
				Thread.sleep(3000);   //wait app has started
			} catch (InterruptedException e) {
				//ingore
			}
			//开启线程进行请求调用
			String url = "http://127.0.0.1:8082/services";
			long start = System.currentTimeMillis();
			taketime = 0;
			for(int i = 0;i<threadCounts;i++){
				new Thread(){
					@Override
					public void run() {
						try {
							if(RequestUtils.sendGetRequest(url)){
								sucessCount.incrementAndGet();
								log.info("send requeslt success");
							}else{
								log.error("send requeslt fail");
								errorCount.incrementAndGet();
							}
						} catch (MalformedURLException e) {
							log.error("send request fail",e);
							errorCount.incrementAndGet();
						} catch (IOException e) {
							log.error("send request fail",e);
							errorCount.incrementAndGet();
						}finally{
							totalCount.incrementAndGet();
						}
					}
				}.start();
			}
			
			while(totalCount.get() != threadCounts){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					//ignore
				}
			}
			
			taketime = System.currentTimeMillis() - start;
		}
		// 停止程序
		CmdUtils.stopApp();
		
		//统计数据
		return new Result(totalCount.get(),sucessCount.get(),errorCount.get(),taketime);
	}
}

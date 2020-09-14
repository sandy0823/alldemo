package com.example.demo.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
	
	/*
	 * args[0]:app 的启动路径 args[1]:线程连接数 args[2]:consul 连接池的target host 连接池数量
	 */
	public static void main(String[] args) {
		String startAPPAbPath = args[0];
		int threadCounts = Integer.valueOf(args[1]);
		String consulPoolConnections = args[2];
		int connectTimeout = Integer.valueOf(args[3]);

		log.info("startAPPAbPath:[{}],threadCounts[{}],consulPoolConnections[{}],connectTimeout[{}]", 
				startAPPAbPath, threadCounts,consulPoolConnections,connectTimeout);

		// 修改application.properties
		String applicationFilePath = startAPPAbPath + "/config/application.properties";
		FileUtils.setValue(applicationFilePath, "spring.cloud.consul.http.max-per-route-connections",
				consulPoolConnections);
		FileUtils.setValue(applicationFilePath, "spring.cloud.consul.http.connection-timeout",
				String.valueOf(connectTimeout));
		for(int i =0;i<10;i++){
			Service service = new Service(threadCounts,consulPoolConnections,startAPPAbPath,10,
					startAPPAbPath+"/result.txt",connectTimeout);
			service.loopExec();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}

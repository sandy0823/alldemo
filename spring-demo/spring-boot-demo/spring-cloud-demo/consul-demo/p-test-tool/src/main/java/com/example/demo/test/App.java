package com.example.demo.test;

import org.apache.commons.lang3.StringUtils;

import com.example.demo.test.model.Config;
import com.example.demo.tool.FileUtils;
import com.example.demo.tool.PropertyUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * A服务调用consul接口获取服务数据，由于A继承 spring cloud consul对接 consul 开源软件，而spring cloud consul 使用<br>
 * http连接池管理对接consul的TCP连接。经过试验，发现在连接池中无建立好的连接（一般发生在A服务重启），并发调用consul接口<br>
 * 会初始化连接，而这部分损耗会直接影响并发数。<br>
 * 本工具主要是测试连接池还未建立好时的可达并发连接数验证。<br>
 * PS：
 * 每次并发调用之后，会暂停2s,
 * @author sandy
 *
 */
@Slf4j
public class App {
	
	public static void main(String[] args) {
		
		Config config = PropertyUtils.loadConfig();
		log.info(config.toString());

		if(StringUtils.isEmpty(config.getServiceStartPath()) || StringUtils.isEmpty(config.getHttpURL())){
			throw new IllegalArgumentException("please check config if is "
					+ "correct,service.start.path or service.jar.name or http.url is empty");
		}
		// 修改application.properties
		String applicationFilePath = config.getServiceStartPath() + "/config/application.properties";
		if(!FileUtils.setValue(applicationFilePath, "spring.cloud.consul.http.max-per-route-connections",
				String.valueOf(config.getHttpConnectPoolSize()))){
			log.error("set connectPoolSize into application.properties of app wrongly.");
			return;
		}
		if(!FileUtils.setValue(applicationFilePath, "spring.cloud.consul.http.connection-timeout",
				String.valueOf(config.getHttpConnectTimeout()))){
			log.error("set connectTimeOut into application.properties of app wrongly.");
			return;
		}
		
		Service service = new Service(config.getParallelThreadsCount(),config.getHttpConnectPoolSize(),
				config.getServiceStartPath(),config.getLoopCounts(),
				config.getServiceStartPath()+"/result.txt",config.getHttpConnectTimeout(),config.getHttpURL(),
				config.isServiceRestart(),config.getServiceStartWait());
		service.loopExec();
		
		System.exit(0);
	}
}

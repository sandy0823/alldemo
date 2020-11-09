package com.example.demo.consulDemo.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecwid.consul.v1.ConsulClient;
import com.example.demo.consulDemo.RequestUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DemoController {
	 private ExecutorService executor = Executors.newFixedThreadPool(1000);
	 @Autowired
	 private ConsulClient consulClient;
    
	@GetMapping("/hello")
	public String hello(){
		log.info("hello");
		return "hello";
	}
	
	@GetMapping("/produce")
	public String produce(){
		log.info("produce");
		return "produce";
	}
	
	@GetMapping("/test")
	public void test(){
		for(int i =0;i<1000;i++){
			executor.submit(new Runnable() {
				@Override
				public void run() {
					try{
						//consulClient.getCatalogServices(null);
						RequestUtils.sendGetRequest("http://192.168.149.128:8500/v1/catalog/services");
					}catch(Throwable ex ){
						log.error(ex.getMessage(),ex);
					}
				}
			});
		}
	}
}

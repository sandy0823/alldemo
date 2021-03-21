package com.summer.simple.service.demo;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloDemo {
	private AtomicBoolean health = new AtomicBoolean(true);
	private static final Logger log = LoggerFactory.getLogger(HelloDemo.class);
	
	@GetMapping("/hello")
   public String hello(){
	   return "hello,my name is service2";
   }
	
	@GetMapping("/ping")
	public String health(HttpServletResponse response){
		log.info("it start to check health");
		if(health.get()){
			log.info("it is health");
			return "OK";
		}else{
			response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
			log.info("it is unhealth");
			return "FAIL";
		}
	}
	
	@GetMapping("/healthy")
	public boolean healthy(){
		health.compareAndSet(false, true);
		return health.get();
	}
	
	@GetMapping("/unhealthy")
	public boolean unhealthy(){
		health.compareAndSet(true, false);
		return health.get();
	}
	
}

package com.summer.simple.service.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloDemo {
	
	@GetMapping("/hello")
   public String hello(){
	   return "hello,my name is service1";
   }
	
	@GetMapping("/health")
	public String health(){
		return "OK";
	}
}

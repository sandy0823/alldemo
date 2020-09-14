package com.example.demo.consulDemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HealthController {
  
	@GetMapping("/health")
	public void health(){
		log.info("i am health");
	}
}

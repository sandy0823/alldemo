package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.catalog.model.CatalogService;
import com.example.demo.service.ConsumerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class DemoController {
	@Autowired
    private ConsumerService consumerService;
	@Autowired
	private ConsulClient consulClient;
	
	@GetMapping("/hello")
	public String hello(){
		log.info("hello");
		return "hello";
	}
	
	@GetMapping("/producer")
	public String getProducer(){
		return consumerService.getProduce();
	}
	
	@GetMapping("/services")
	public Response<List<CatalogService>> getAllService(){
		return consulClient.getCatalogService("consul-service-demo-producer", null);
	}
}

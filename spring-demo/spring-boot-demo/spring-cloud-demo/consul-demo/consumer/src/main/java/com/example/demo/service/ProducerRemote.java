package com.example.demo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("consul-service-demo-producer")
public interface ProducerRemote {
	@GetMapping("/produce")
    String producer();
}

package com.example.demo.consulDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ConsulDemoApplication 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(ConsulDemoApplication.class, args);
    }
}

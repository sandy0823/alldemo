package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
   @Autowired
   private ProducerRemote producerRemote;
   
   public String getProduce(){
	   return producerRemote.producer();
   }
}

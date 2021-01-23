package com.summer.demo.springboot.redis.jedis.config;

import java.lang.reflect.Field;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.DefaultJedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisClientConfigurationBuilder;
import org.springframework.util.ReflectionUtils;

@Configuration
public class JedisConfig {
	@Bean
	public JedisClientConfigurationBuilderCustomizer builderCustomizers(){
		JedisClientConfigurationBuilderCustomizer customizer = new JedisClientConfigurationBuilderCustomizer(){

			@SuppressWarnings("rawtypes")
			@Override
			public void customize(JedisClientConfigurationBuilder clientConfigurationBuilder) {
				DefaultJedisClientConfigurationBuilder poolBuilder = (DefaultJedisClientConfigurationBuilder)clientConfigurationBuilder;
				Field field = ReflectionUtils.findField(DefaultJedisClientConfigurationBuilder.class, "poolConfig");
			    if(null != field){
			    	ReflectionUtils.makeAccessible(field);
			    	GenericObjectPoolConfig poolConfig = (GenericObjectPoolConfig)ReflectionUtils.getField(field, poolBuilder);
			    	poolConfig.setTestOnBorrow(true);
			    	poolConfig.setTestOnCreate(true);
			    	poolConfig.setTestOnReturn(true);
			    	poolConfig.setTestWhileIdle(true);
			    }
			}
			
		};
		
		return customizer;	
	}
}

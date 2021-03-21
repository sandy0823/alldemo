package com.summer.demo.springboot.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RediesDemoController {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@GetMapping("/jedis/hello")
	public Object testJedis() {
		Object value = redisTemplate.opsForValue().get("hello");
		if (value == null) {
			redisTemplate.opsForValue().set("hello", "jedis");
			value = redisTemplate.opsForValue().get("hello");
		}
		return value;
	}

	@GetMapping("/lettuce/hello")
	public Object testLettuce() {
		Object value = redisTemplate.opsForValue().get("hello");
		if (value == null) {
			redisTemplate.opsForValue().set("hello", "lettuce");
			value = redisTemplate.opsForValue().get("hello");
		}
		return value;
	}

	@GetMapping("/query/hello")
	public Object testSection(@RequestParam("key") String key) {
		Object value = redisTemplate.opsForValue().get(key);
		if (value == null) {
			redisTemplate.opsForValue().set(key, key);
			value = redisTemplate.opsForValue().get(key);
		}
		return value;
	}
}

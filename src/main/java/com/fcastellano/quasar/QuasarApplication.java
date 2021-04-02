package com.fcastellano.quasar;

import com.fcastellano.quasar.model.Communication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;

@SpringBootApplication
public class QuasarApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuasarApplication.class, args);
	}

	@Bean
	public RedisTemplate<String, HashMap<String, Communication>> redisTemplate(RedisConnectionFactory connectionFactory){
		RedisTemplate<String, HashMap<String, Communication>> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		return template;
	}
}

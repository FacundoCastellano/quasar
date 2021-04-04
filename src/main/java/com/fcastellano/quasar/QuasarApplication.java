package com.fcastellano.quasar;

import com.fcastellano.quasar.model.Communication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashMap;

@SpringBootApplication
@EnableSwagger2
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
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
}

package com.example.lujuria;

import com.example.lujuria.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class LujuriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LujuriaApplication.class, args);
	}

}

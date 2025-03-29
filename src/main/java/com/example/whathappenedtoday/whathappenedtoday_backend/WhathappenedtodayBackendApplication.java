package com.example.whathappenedtoday.whathappenedtoday_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example.whathappenedtoday.controller",
		"com.example.whathappenedtoday.service",
		"com.example.whathappenedtoday.whathappenedtoday_backend" })
@EntityScan("com.example.whathappenedtoday.model")
@EnableMongoRepositories("com.example.whathappenedtoday.repository")
public class WhathappenedtodayBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WhathappenedtodayBackendApplication.class, args);
	}

}

package com.example.rewise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class RewiseApplication {

	public static void main(String[] args) {
		SpringApplication.run(RewiseApplication.class, args);
	}

}

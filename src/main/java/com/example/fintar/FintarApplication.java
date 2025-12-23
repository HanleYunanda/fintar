package com.example.fintar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class FintarApplication {

	public static void main(String[] args) {
		SpringApplication.run(FintarApplication.class, args);
	}

}

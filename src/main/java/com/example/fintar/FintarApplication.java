package com.example.fintar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.scheduling.annotation.EnableScheduling
public class FintarApplication {

  public static void main(String[] args) {
    SpringApplication.run(FintarApplication.class, args);
  }
}

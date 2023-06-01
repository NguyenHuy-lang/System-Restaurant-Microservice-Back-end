package com.micro.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.micro.app")
public class FoodMicroservice {
    public static void main(String[] args) {
        SpringApplication.run(FoodMicroservice.class, args);
    }
}
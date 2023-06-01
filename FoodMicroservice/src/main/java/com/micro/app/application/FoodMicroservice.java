package com.micro.app.application;

import com.micro.app.model.Food;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodMicroservice {
    public static void main(String[] args) {
        SpringApplication.run(FoodMicroservice.class, args);
    }
}
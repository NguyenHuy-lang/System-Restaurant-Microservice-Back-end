package com.micro.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.micro.app")
public class OrderMicroservice {
    public static void main(String[] args) {
        SpringApplication.run(OrderMicroservice.class, args);
    }
}
package com.micro.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
public class OrderController {
    @PostMapping("/")
    ResponseEntity<Void> createBookingForUser() {
        Integer userId = 1;
        return ResponseEntity.ok().build();
    }
}

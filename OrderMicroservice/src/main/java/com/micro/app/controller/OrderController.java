package com.micro.app.controller;

import com.micro.app.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class OrderController {
    private final BookingRepository bookingRepository;
    @PostMapping("/")
    ResponseEntity<Void> createBookingForUser() {
        Integer userId = 2;
        bookingRepository.createBookingForUser(userId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{bookingId}/tables/{tableId}")
    ResponseEntity<Void> addTableToBookingForUser(@PathVariable(name = "bookingId") Integer bookingId,
                                                  @PathVariable(name = "tableId") Integer tableId) {

    }
}

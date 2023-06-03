package com.micro.app.controller;


import com.micro.app.model.Food;
import com.micro.app.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class OrderController {
    private final BookingRepository bookingRepository;
    // api tao booking cho khach hang
    @PostMapping("/")
    ResponseEntity<Void> createBookingForUser() {
        Integer userId = 2;
        bookingRepository.createBookingForUser(userId);
        return ResponseEntity.ok().build();
    }


    // api them ban an vao booking
    @PostMapping("/{bookingId}/tables/{tableId}")
    ResponseEntity<Void> addTableToBookingForUser(@PathVariable(name = "bookingId") Integer bookingId,
                                                  @PathVariable(name = "tableId") Integer tableId) {
        bookingRepository.addTableToBookingForUser(bookingId, tableId);
        return ResponseEntity.ok().build();
    }
    // api xoa table khoi booking
    @DeleteMapping("/{bookingId}/tables/{tableId}")
    ResponseEntity<Void> deleteTableToBookingForUser(@PathVariable(name = "bookingId") Integer bookingId,
                                                  @PathVariable(name = "tableId") Integer tableId) {
        bookingRepository.deleteTableToBookingForUser(bookingId, tableId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{bookingId}/tables/{tableId}/foods/{foodId}")
    ResponseEntity<Void> addFoodToTableToBookingForUser(@PathVariable(name = "bookingId") Integer bookingId,
                                                        @PathVariable(name = "tableId") Integer tableId,
                                                        @PathVariable(name = "foodId") Integer foodId,
                                                        @RequestBody Map<String, Integer> request) {
        Integer quantity = request.get("quantity");
        String url = "localhost:8082/api/v1/foods/" + foodId;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Food> response = restTemplate.getForEntity(url, Food.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            Food food = response.getBody();
            // Do something with the array of Food objects
            Integer bookedTableId = bookingRepository.getBookedTableId(bookingId, tableId);
            bookingRepository.addFoodToTableToBookingForUser(bookedTableId, foodId, food.getPrice(), quantity);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/{bookingId}/tables/{tableId}/foods/{foodId}")
    ResponseEntity<Void> updateQuantityFoodToTableToBookingForUser(@PathVariable(name = "bookingId") Integer bookingId,
                                                                   @PathVariable(name = "tableId") Integer tableId,
                                                                   @PathVariable(name = "foodId") Integer foodId,
                                                                   @RequestBody Map<String, Integer> request) {
        Integer quantity = request.get("quantity");
        Integer bookedTableId = bookingRepository.getBookedTableId(bookingId, tableId);

        bookingRepository.updateFoodToTableToBookingForUser(bookedTableId, foodId, quantity);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{bookingId}/tables/{tableId}/foods/{foodId}")
    ResponseEntity<Void> deleteFoodToTableToBookingForUser(@PathVariable(name = "bookingId") Integer bookingId,
                                                           @PathVariable(name = "tableId") Integer tableId,
                                                           @PathVariable(name = "foodId") Integer foodId){
        Integer bookedTableId = bookingRepository.getBookedTableId(bookingId, tableId);
        bookingRepository.deleteFoodToTableToBookingForUser(bookedTableId, foodId);
        return ResponseEntity.ok().build();
    }
}

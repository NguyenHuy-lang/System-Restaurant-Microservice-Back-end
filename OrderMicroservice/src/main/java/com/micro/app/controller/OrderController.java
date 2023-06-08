package com.micro.app.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.micro.app.api.RestApi;
import com.micro.app.model.*;
import com.micro.app.repository.BookingRepository;
import com.micro.app.request.UpdateStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.awt.print.Book;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@CrossOrigin
@Transactional
public class OrderController {
    private final BookingRepository bookingRepository;
    private RestApi restApi = new RestApi();
    // api tao booking cho khach hang

    @PostMapping("/")
    ResponseEntity<Void> createBookingForUser(Authentication authentication) {
        User user = restApi.getUserRequest(authentication);
        bookingRepository.createBookingForUser(user.getId());
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
        String url = "http://localhost:8082/api/v1/foods/" + foodId;
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
    @PutMapping("/{bookingId}/tables/{tableId}/foods/{foodId}")
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

    @GetMapping("/customer/{email}/pending")
    ResponseEntity<List<Booking>> getListBookingsByCustomerEmail(@PathVariable(name = "email") String email, HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
            System.out.println("Access Token: " + accessToken);
        }
        User user = restApi.getUserRequest(email);
        List<Map<String, Object>> bookingData = bookingRepository.getBookingPendingOfCustomer(user.getId());
        List<Booking> bookings = new ArrayList<>();
        Set<Integer> processedBookingIds = new HashSet<>();

        for (Map<String, Object> row : bookingData) {
            Integer bookingId = (Integer) row.get("booking_id");

            // Check if the booking ID has already been processed
            if (!processedBookingIds.contains(bookingId)) {
                Integer id = (int) row.get("Id");
                Date createDateAsDate = new Date(((Timestamp) row.get("create_date")).getTime());
                String status = (String) row.get("status");

                Booking booking = Booking.builder()
                        .id(id)
                        .createDate(createDateAsDate)
                        .status(status)
                        .customer(new Customer())
                        .build();
                booking.setBookedTableList(new ArrayList<>());

                Integer customerId = (int) row.get("customer_id");
                Integer tableId = (int) row.get("table_id");
                Integer foodId = (int) row.get("food_id");
                Integer quantity = (int) row.get("quantity");
                Integer price = (int) row.get("price");

                User userOfBooking = restApi.getUserByIdFromUserMicroservice(customerId, accessToken);
                Table tableOfBooking = restApi.getTableByIdFromTableMicroservice(tableId);
                Food food = restApi.getFoodByIdFromMicroservice(foodId);
                DetailFood detailFood = DetailFood.builder()
                        .food(food)
                        .price(price)
                        .quantity(quantity)
                        .build();

                boolean foundTable = false;
                for (BookedTable bookedTable : booking.getBookedTableList()) {
                    if (bookedTable.getTable().getId() == tableOfBooking.getId()) {
                        foundTable = true;
                        if (bookedTable.getDetailFoodList() == null) {
                            bookedTable.setDetailFoodList(new ArrayList<>());
                        }
                        bookedTable.getDetailFoodList().add(detailFood);
                        break;
                    }
                }

                if (!foundTable) {
                    BookedTable bookedTable = BookedTable.builder()
                            .table(tableOfBooking)
                            .detailFoodList(new ArrayList<>())
                            .build();
                    bookedTable.getDetailFoodList().add(detailFood);
                    booking.getBookedTableList().add(bookedTable);
                }

                bookings.add(booking);
                processedBookingIds.add(bookingId);
            }
        }

        return ResponseEntity.ok().body(bookings);
    }
    @PutMapping("/booking/{bookingId}/status")
    public ResponseEntity<String> updateBookingStatus(@PathVariable(name = "bookingId") Integer bookingId, @RequestBody UpdateStatusRequest updateStatusRequest, HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
            System.out.println("Access Token: " + accessToken);
        }
        try {
            // Retrieve the booking from the database
            Optional<Booking> booking = bookingRepository.findById(bookingId);

            if (booking == null) {
                // Booking not found
                return ResponseEntity.notFound().build();
            }

            // Validate the new status
            if (!isValidStatus(updateStatusRequest.getStatus())) {
                // Invalid status provided
                return ResponseEntity.badRequest().body("Invalid status.");
            }

            // Update the status
            booking.get().setStatus(updateStatusRequest.getStatus());


            // Return a success response
            return ResponseEntity.ok("Booking status updated successfully.");
        } catch (Exception e) {
            // Handle any exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the booking status.");
        }
    }

    private boolean isValidStatus(String status) {
        // Define the list of valid status options
        List<String> validStatusOptions = Arrays.asList("pending", "confirmed", "paid", "cancelled");

        // Check if the provided status is in the list of valid options
        return validStatusOptions.contains(status);
    }
}

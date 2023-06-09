package com.micro.app.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.micro.app.api.RestApi;
import com.micro.app.model.*;
import com.micro.app.repository.BookingRepository;
import com.micro.app.request.UpdateStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.awt.print.Book;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@CrossOrigin
@Transactional
public class OrderController {
    private final BookingRepository bookingRepository;
    private RestApi restApi = new RestApi();
    // api tao booking cho khach hang
    @Autowired
    private EntityManager entityManager;

    public List<Booking> getBookingByCustomer(User user) {
        return bookingRepository.getBookingOfCustomer(user.getId());
    }

    @GetMapping("/payment")
    ResponseEntity<List<Booking>> getListBookingToPaymentOfCus(Authentication authentication) {
        User user = restApi.getUserRequest(authentication);
        List<Booking> listBooking = bookingRepository
                                                .getBookingOfCustomer(user.getId())
                                                .stream()
                                                .filter(x -> x.getStatus().equals("received")).
                                                collect(Collectors.toList());

        listBooking.stream().forEach(booking -> {
            booking.setTotal(0);
            booking.setCustomer(Customer.builder().user(user).build());
            if (booking.getBookedTableList() != null) {
                booking.getBookedTableList().forEach(bookedTable -> {
                    if (bookedTable.getId() != null) {
                        Integer tableId = bookingRepository.getTableIdByBookedTableId(bookedTable.getId());
                        Table table = restApi.getTableByIdFromTableMicroservice(tableId);
                        bookedTable.setTable(table);
                    }
                    if (bookedTable.getDetailFoodList() != null) {
                        bookedTable.getDetailFoodList().stream().forEach(detailFood -> {
                            Integer foodId = bookingRepository.getFoodByDetailFoodId(detailFood.getId());
                            Food food = restApi.getFoodByIdFromMicroservice(foodId);
                            detailFood.setFood(food);
                            detailFood.setPrice(bookingRepository.getPriceByDetailFoodId(detailFood.getId()));
                            detailFood.setQuantity(bookingRepository.getQuantityByDetailFoodId(detailFood.getId()));
                            booking.setTotal(booking.getTotal() + detailFood.getPrice() * detailFood.getQuantity());
                        });
                    }
                });
            }
        });
        return ResponseEntity.ok().body(listBooking);
    }

    @GetMapping("/")
    ResponseEntity<List<Booking>> getAllBookingForUser(Authentication authentication) {
        User user = restApi.getUserRequest(authentication);
        return ResponseEntity.ok().body(bookingRepository
                .getBookingOfCustomer(user.getId()));
    }

    @PostMapping("/")
    ResponseEntity<Booking> createBookingForUser(Authentication authentication) {
        User user = restApi.getUserRequest(authentication);
        bookingRepository.createBookingForUser(user.getId());
        return ResponseEntity.ok().body(getLatestBookingByCustomer(user.getId()));
    }


    // api them ban an vao booking
    @PostMapping("/{bookingId}/tables/{tableId}")
    ResponseEntity<Table> addTableToBookingForUser(@PathVariable(name = "bookingId") Integer bookingId,
                                                  @PathVariable(name = "tableId") Integer tableId) {
        Integer found = bookingRepository.selectTableToBookingForUser(bookingId, tableId);
        if (found != null) {
            return ResponseEntity.badRequest().build();
        }
        bookingRepository.addTableToBookingForUser(bookingId, tableId);
        return ResponseEntity.ok().body(restApi.getTableByIdFromTableMicroservice(tableId));
    }

    @GetMapping("/{bookingId}/tables")
    ResponseEntity<List<Table>> getListTableOfBooking(@PathVariable(name = "bookingId") Integer bookingId) {
        List<Integer> listTableId = bookingRepository.getListTableId(bookingId);
        List<Table> res = new ArrayList<>();
        for (Integer tableId : listTableId) {
            res.add(restApi.getTableByIdFromTableMicroservice(tableId));
        }
        return ResponseEntity.ok().body(res);
    }

    // api xoa table khoi booking
    @DeleteMapping("/{bookingId}/tables/{tableId}")
    ResponseEntity<Void> deleteTableToBookingForUser(@PathVariable(name = "bookingId") Integer bookingId,
                                                  @PathVariable(name = "tableId") Integer tableId) {
        bookingRepository.deleteTableToBookingForUser(bookingId, tableId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{bookingId}/tables/{tableId}/foods/{foodId}")
    ResponseEntity<List<DetailFood>> addFoodToTableToBookingForUser(@PathVariable(name = "bookingId") Integer bookingId,
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
            if (bookingRepository.existFoodInTable(bookedTableId,foodId) == null) {
                bookingRepository.addFoodToTableToBookingForUser(bookedTableId, foodId, food.getPrice(), quantity);
            } else {
                bookingRepository.updateFoodToTableToBookingForUser(bookedTableId, foodId, quantity);
            }
        }

        Integer bookedTableId = bookingRepository.getBookedTableId(bookingId, tableId);

        List<Map<String, Object>> rows = bookingRepository.getListFoodOfTable(bookedTableId);
        List<DetailFood> listFood = new ArrayList<>();
        rows.stream().forEach(row -> {
            Food food = restApi.getFoodByIdFromMicroservice((int) row.get("food_id"));
            int quantity2 = (int) row.get("quantity");
            Integer price = (int) row.get("price");
            Integer ID = (int) row.get("ID");
            DetailFood detailFood = DetailFood
                    .builder()
                    .food(food)
                    .quantity(quantity2)
                    .price(price)
                    .id(ID)
                    .build();
            listFood.add(detailFood);
        });
        return ResponseEntity.ok().body(listFood);

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

    @GetMapping("/{bookingId}/tables/{tableId}/foods")
    ResponseEntity<List<DetailFood>> getListFoodOfTableOfBooking(@PathVariable(name = "bookingId") Integer bookingId,
                                                                   @PathVariable(name = "tableId") Integer tableId
                                                                   ) {
        Integer bookedTableId = bookingRepository.getBookedTableId(bookingId, tableId);

        List<Map<String, Object>> rows = bookingRepository.getListFoodOfTable(bookedTableId);
        List<DetailFood> listFood = new ArrayList<>();
        rows.stream().forEach(row -> {
            Food food = restApi.getFoodByIdFromMicroservice((int) row.get("food_id"));
            Integer quantity = (int) row.get("quantity");
            Integer price = (int) row.get("price");
            Integer ID = (int) row.get("ID");
            DetailFood detailFood = DetailFood
                    .builder()
                    .food(food)
                    .quantity(quantity)
                    .price(price)
                    .id(ID)
                    .build();
            listFood.add(detailFood);
        });
        return ResponseEntity.ok().body(listFood);
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
                        .customer(Customer.builder().user(user).build())
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
    public Booking getLatestBookingByCustomer(Integer customerId) {
        Query query = entityManager.createNativeQuery("SELECT * FROM tbl_booking " +
                        "WHERE customer_id = :customerId " +
                        "ORDER BY create_date DESC LIMIT 1", Booking.class)
                .setParameter("customerId", customerId);

        List<Booking> bookings = query.getResultList();
        if (!bookings.isEmpty()) {
            return bookings.get(0);
        }

        return null; // Return null if no booking found
    }

    public Integer getLatestFoodInTableBooking(Integer bookedTableId, Integer foodId ) {
        Integer result = null;

        try {
            String sql = "SELECT a.ID FROM tbl_detail_food a " +
                    "WHERE a.booked_table_id = :bookedTableId AND a.food_id = :foodId " +
                    "ORDER BY ID DESC LIMIT 1";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("bookedTableId", bookedTableId);
            query.setParameter("foodId", foodId);

            result = (Integer) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            // Handle no result found
        } catch (Exception e) {
            // Handle other exceptions
        }
        return null;
// Use the result as needed

    }
    private boolean isValidStatus(String status) {
        // Define the list of valid status options
        List<String> validStatusOptions = Arrays.asList("pending", "confirmed", "paid", "cancelled");

        // Check if the provided status is in the list of valid options
        return validStatusOptions.contains(status);
    }
}

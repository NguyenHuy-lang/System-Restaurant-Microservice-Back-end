package com.micro.app.controller;
import com.micro.app.api.RestApi;
import com.micro.app.model.*;
import com.micro.app.repository.BookingRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import javax.transaction.Transactional;

@RestController
@RequestMapping("api/v1/pay")
@CrossOrigin
@Log4j2
public class PaypalController {

    @Autowired
    PaypalService service;
    @Autowired
    private BookingRepository bookingRepository;
    public static final String SUCCESS_URL = "api/v1/pay/success";
    public static final String CANCEL_URL = "api/v1/pay/cancel";
    private RestApi restApi = new RestApi();


    @GetMapping("/{bookingId}")
    public String payment(@PathVariable(name = "bookingId") Integer bookingId,  Authentication authentication) {
        User user = restApi.getUserRequest(authentication);
        Booking booking = bookingRepository.findById(bookingId).get();
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
        try {
            Payment payment = service.createPayment(booking.getTotal() * 1.0, "USD", "payPal",
                    "sale", "this is new description", "http://localhost:8083/" + CANCEL_URL,
                    "http://127.0.0.1:5500/resultPayment.html");
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping("success")
    @Transactional
    public String successPay(@RequestParam("bookingId") Integer bookingId,
                             @RequestParam("paymentId") String paymentId,
                             @RequestParam("PayerID") String payerId,
                             Authentication authentication) {
        Booking booking = bookingRepository.findById(bookingId).get();
        booking.setStatus("payment");
        return "success";
    }

}
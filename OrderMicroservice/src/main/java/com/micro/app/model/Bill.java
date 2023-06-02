package com.micro.app.model;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_bill")
@Builder
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date timePayment;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_id")
    private Booking booking;
    private Integer totalMonney;
    private String paymentMethod;
}

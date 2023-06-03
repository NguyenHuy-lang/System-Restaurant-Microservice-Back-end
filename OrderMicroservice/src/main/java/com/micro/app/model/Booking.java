package com.micro.app.model;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tbl_booking")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToMany
    @JoinColumn(name = "booking_id")
    private List<BookedTable> bookedTableList;
    private Date checkIn;
    @Transient
    private Customer customer;
    private String status;
    private Date createDate;

}

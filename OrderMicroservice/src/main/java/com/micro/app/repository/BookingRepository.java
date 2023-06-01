package com.micro.app.repository;

import com.micro.app.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Modifying
    @Query(value = "INSERT INTO tbl_order" +
            "(createDate, " + "status, customerID) VALUES " +
            "(CURRENT_TIMESTAMP, 'pending', " +
            ":customerID)", nativeQuery = true)
    public void createBookingForUser(@Param("customerID") Integer userId);
}

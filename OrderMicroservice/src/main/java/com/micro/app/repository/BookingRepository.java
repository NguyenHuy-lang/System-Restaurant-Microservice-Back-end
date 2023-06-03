package com.micro.app.repository;
import com.micro.app.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO tbl_booking" +
            "(create_date, status, customer_id) VALUES " +
            "(CURRENT_TIMESTAMP, 'pending', " +
            ":customerID)", nativeQuery = true)
    public int createBookingForUser(@Param("customerID") Integer userId);
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO tbl_booked_table(booking_id, table_id) values " +
            "(:bookingId, :tableId)", nativeQuery = true)
    public int addTableToBookingForUser(@Param("bookingId") Integer booking_id,
                                         @Param("tableId") Integer table_id);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM tbl_booked_table tmp " +
            "WHERE tmp.booking_id = :bookingId and tmp.table_id = :tableId", nativeQuery = true)
    public int deleteTableToBookingForUser(@Param("bookingId") Integer booking_id,
                                            @Param("tableId") Integer table_id);

    @Transactional
    @Query(value = "SELECT tmp.id FROM tbl_booked_table tmp WHERE " +
            "tmp.booking_id = :bookingId and tmp.table_id = :tableId LIMIT 1", nativeQuery = true)
    public Integer getBookedTableId(@Param("bookingId") Integer bookingId,
                                    @Param("tableId") Integer tableId);
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO tbl_detail_food(booked_table_id, food_id, quantity, price) " +
            "values(:bookedTableId, :foodId, :quantity, :price) ", nativeQuery = true)
    public int addFoodToTableToBookingForUser(@Param("bookedTableId") Integer bookedTableId,
                                               @Param("foodId") Integer foodId,
                                               @Param("price") Integer price,
                                               @Param("quantity")Integer quantity);
    @Transactional
    @Modifying
    @Query(value = "UPDATE tbl_detail_food tmp " +
            "SET tmp.quantity = :quantity where " +
            "tmp.booked_table_id = :bookedTableId and tmp.food_id = :foodId ", nativeQuery = true)
    public int updateFoodToTableToBookingForUser(@Param("bookedTableId") Integer bookedTableId,
                                                  @Param("foodId") Integer foodId,
                                                  @Param("quantity")Integer quantity);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM tbl_detail_food tmp WHERE tmp.booked_table_id = :bookedTableId and tmp.food_id = :foodId", nativeQuery = true)
    public int deleteFoodToTableToBookingForUser(@Param("bookedTableId") Integer bookedTableId,
                                                  @Param("foodId") Integer foodId);
}

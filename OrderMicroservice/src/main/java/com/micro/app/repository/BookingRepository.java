package com.micro.app.repository;
import com.micro.app.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

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
    @Modifying
    @Query(value = "SELECT a.Id, a.create_date , a.status, a.customer_id, " +
            "b.booking_id , b.table_id, c.food_id, c.quantity, c.price " +
            " from tbl_booking a" +
            " JOIN tbl_booked_table b" +
            " JOIN tbl_detail_food c" +
            " ON a.ID = b.booking_id and b.ID = c.booked_table_id " +
            " where a.customer_id = :customerId", nativeQuery = true)
    @Transactional
    List<Map<String, Object>> getBookingPendingOfCustomer
            (@Param("customerId")Integer customerId);
    @Transactional
    @Modifying
    @Query(value = "UPDATE tbl_booking tmp " +
            "SET tmp.status = :status where " +
            "tmp.id = :bookingId ", nativeQuery = true)
    int updateStatusBooking(@Param("bookingId") Integer bookingId,
                                    @Param("status") String status);
}

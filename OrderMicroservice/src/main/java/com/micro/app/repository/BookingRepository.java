package com.micro.app.repository;
import com.micro.app.model.BookedTable;
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
    @Query(value = "SELECT * FROM tbl_booking a where a.customer_id =:customerId", nativeQuery = true)
    public List<Booking> getBookingOfCustomer(@Param("customerId") Integer customerId);
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO tbl_booking" +
            "(create_date, status, customer_id) VALUES " +
            "(CURRENT_TIMESTAMP, 'pending', " +
            ":customerID)", nativeQuery = true)
    public int createBookingForUser(@Param("customerID") Integer userId);


    @Transactional
    @Query(value = "SELECT a.ID FROM tbl_booked_table a " +
            "where a.booking_id = :bookingId and a.table_id = :tableId", nativeQuery = true)
    public Integer selectTableToBookingForUser(@Param("bookingId") Integer booking_id,
                                                   @Param("tableId") Integer table_id);



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

    @Query(value = "SELECT a.ID FROM tbl_detail_food a WHERE a.booked_table_id = :bookedTableId AND a.food_id = :foodId", nativeQuery = true)
    public Integer existFoodInTable(@Param("bookedTableId") Integer bookedTableId, @Param("foodId") Integer foodId);
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO tbl_detail_food(booked_table_id, food_id, quantity, price) " +
            "values(:bookedTableId, :foodId, :quantity, :price) ", nativeQuery = true)
    public int addFoodToTableToBookingForUser(@Param("bookedTableId") Integer bookedTableId,
                                               @Param("foodId") Integer foodId,
                                               @Param("price") Integer price,
                                               @Param("quantity")Integer quantity);
    @Query(value = "SELECT a.table_id FROM tbl_booked_table a WHERE a.booking_id = :bookingId", nativeQuery = true)
    public List<Integer> getListTableId(@Param("bookingId") Integer bookingId);
    @Query(value = "SELECT a.ID, a.food_id , a.price, a.quantity FROM tbl_detail_food a WHERE a.booked_table_id = :bookedTableId", nativeQuery = true)
    public List<Map<String, Object>> getListFoodOfTable(@Param("bookedTableId") Integer bookedTableId);

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
    @Query(value = "SELECT a.table_id FROM tbl_booked_table a WHERE a.ID = :bookedTableId", nativeQuery = true)
    int getTableIdByBookedTableId(@Param("bookedTableId") Integer bookedTableId);

    @Query(value = "SELECT a.food_id FROM tbl_detail_food a WHERE a.ID = :detailFoodId", nativeQuery = true)
    int getFoodByDetailFoodId(@Param("detailFoodId") Integer detailFoodId);

    @Query(value = "SELECT a.quantity FROM tbl_detail_food a WHERE a.ID = :detailFoodId", nativeQuery = true)
    int getQuantityByDetailFoodId(@Param("detailFoodId") Integer detailFoodId);

    @Query(value = "SELECT a.price FROM tbl_detail_food a WHERE a.ID = :detailFoodId", nativeQuery = true)
    int getPriceByDetailFoodId(@Param("detailFoodId") Integer detailFoodId);
}

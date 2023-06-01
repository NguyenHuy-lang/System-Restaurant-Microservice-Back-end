CREATE TABLE tbl_booked_table(
    ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    booking_id INT,
    table_id INT,
    CONSTRAINT fk_booked_table_booking foreign key (booking_id)
    references tbl_booking(ID) on delete cascade on update cascade
)
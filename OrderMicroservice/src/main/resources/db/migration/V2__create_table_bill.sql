CREATE TABLE tbl_bill(
    ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    payment_time DATE,
    payment_method varchar(40),
    booking_id INT,
    CONSTRAINT fk_bill_booking foreign key (booking_id)
    references tbl_booking(ID) on delete cascade on update cascade
)
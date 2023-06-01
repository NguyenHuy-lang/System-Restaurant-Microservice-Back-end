CREATE TABLE tbl_booking(
    ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    create_date DATETIME,
    status varchar(40),
    check_in DATETIME,
    customer_id int
)
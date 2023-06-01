CREATE TABLE tbl_bill(
    ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    create_date DATE,
    status varchar(40),
    check_in DATE,
    customer_id int
)
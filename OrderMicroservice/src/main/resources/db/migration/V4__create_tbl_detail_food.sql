CREATE TABLE tbl_detail_food(
    ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    booked_table_id INT,
    food_id INT,
    quantity INT,
    price INT,
    CONSTRAINT fk_detail_food_booked_table foreign key (booked_table_id)
    references tbl_booked_table(ID) on delete cascade on update cascade
)
CREATE TABLE tbl_combo(
    ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    foodId INT,
    CONSTRAINT fk_combo_food foreign key (foodId)
    references tbl_food(ID) on delete cascade on update cascade
)
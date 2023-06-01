CREATE TABLE tbl_combo_dish(
    ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    quantity INT,
    dishId INT,
    comboId INT,
    CONSTRAINT fk_combo_dish_dish foreign key(dishId)
    references tbl_dish(ID) on delete cascade on update cascade,
    CONSTRAINT fk_combo_dish_combo foreign key(comboId)
    references tbl_combo(ID) on delete cascade on update cascade
)
CREATE TABLE tbl_dish(
                         ID INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                         foodId INT,
                         CONSTRAINT fk_dish_food foreign key (foodId)
                             references tbl_food(ID) on delete cascade on update cascade
)
package com.micro.app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_combo_dish")
@Data
public class ComboDish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "dishId")
    private Dish dish;
    private Integer quantity;
}

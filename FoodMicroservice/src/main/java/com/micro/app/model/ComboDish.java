package com.micro.app.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tbl_combo_dish")
@Data
public class ComboDish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
//    @Column(name = "dishId")
    private Dish dish;
    private Integer quantity;
}

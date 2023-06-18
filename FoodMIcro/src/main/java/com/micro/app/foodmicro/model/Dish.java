package com.micro.app.foodmicro.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Data
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "tbl_dish")
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "foodId")
    private Food food;
}

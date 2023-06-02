package com.micro.app.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
//    @Column(name = "foodId")
    private Food food;
}

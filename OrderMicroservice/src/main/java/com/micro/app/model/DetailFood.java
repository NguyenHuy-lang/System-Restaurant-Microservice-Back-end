package com.micro.app.model;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_detail_food")
@Builder
public class DetailFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer quantity;
    @Transient
    private Food food;
    private Integer price;
}

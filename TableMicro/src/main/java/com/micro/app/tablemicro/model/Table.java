package com.micro.app.tablemicro.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Table(name = "tbl_table")
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String status;
    private Integer capacity;

}

package com.micro.app.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@javax.persistence.Table(name = "tbl_booked_table")
@Builder
public class BookedTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Transient
    private Table table;
    @OneToMany
    @JoinColumn(name = "booked_table_id")
    private List<DetailFood> detailFoodList;
}

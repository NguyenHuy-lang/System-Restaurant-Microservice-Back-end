package com.micro.app.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Table {
    private Integer id;
    private String name;
    private String status;
    private Integer capacity;

}

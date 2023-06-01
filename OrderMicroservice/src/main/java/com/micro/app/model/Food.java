package com.micro.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.persistence.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Food {
    private Integer id;
    private String name;
    private Integer price;
    private String type;
}

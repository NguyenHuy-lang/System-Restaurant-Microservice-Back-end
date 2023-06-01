package com.micro.app.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Customer {
    private String phone;
    private String address;
}

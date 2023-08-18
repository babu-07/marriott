package com.microshop.productservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "product")
public class Product {

    @Id
    private Long id;
    private String name;
    private String description;
    private String review;
    private Integer quantity;
    private double price;
    @Version
    private Long version;
}

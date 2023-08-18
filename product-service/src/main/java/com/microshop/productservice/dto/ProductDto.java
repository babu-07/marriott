package com.microshop.productservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductDto {

    private Long id;
    @NotEmpty
    @NotNull
    private String name;
    private String description;
    private String review;
    private Integer quantity;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private double price;
}

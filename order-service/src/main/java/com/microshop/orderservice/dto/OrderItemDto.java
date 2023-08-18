package com.microshop.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderItemDto {
    private Long itemId;
    private Long id;
    @NotNull
    private Long productId;
    @NotNull
    private Integer quantity;
}

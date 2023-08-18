package com.microshop.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderDto {
    private Long id;
    @NotNull
    private Long customerId;
    @NotEmpty
    @Valid
    private List<OrderItemDto> items;
    private LocalDateTime orderDate;
    private double totalAmount;
}

package com.microshop.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,fetch =FetchType.LAZY)
    private List<OrderItem> items;
    private LocalDateTime orderDate;
    private double totalAmount;

}


package com.ecommerce.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "hardware_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HardwareOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String customerEmail;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double totalPrice; // calcularemos consumiendo el catalog-services

    @Column(nullable = false, length = 20)
    private String status; //"creada pagada u enviadA"

    @Column(nullable = false)
    private LocalDateTime orderDate;
}

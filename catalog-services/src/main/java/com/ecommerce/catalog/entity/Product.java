package com.ecommerce.catalog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name; // ej: "Laptop ASUS TUF F15" o "Procesador AMD Ryzen 3 3200G"

    @Column(nullable = false)
    private String category; // ej: "Laptops", "Componentes"

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;
}
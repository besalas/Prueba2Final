package com.ecommerce.inventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId; // Este ID pertenece a catalog-services

    @Column(nullable = false, length = 50)
    private String warehouseCode; // Ej: "BODEGA-NORTE", "BODEGA-CENTRAL"

    @Column(nullable = false)
    private Integer availableQuantity;
}

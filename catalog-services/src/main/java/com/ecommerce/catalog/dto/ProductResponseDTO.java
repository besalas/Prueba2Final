package com.ecommerce.catalog.dto;

import lombok.Data;

@Data
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String category;
    private Double price;
    private Integer stock;
}
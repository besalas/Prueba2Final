package com.ecommerce.inventory.dto;

import lombok.Data;

@Data
public class InventoryResponseDTO {
    private Long id;
    private Long productId;
    private String warehouseCode;
    private Integer availableQuantity;
}
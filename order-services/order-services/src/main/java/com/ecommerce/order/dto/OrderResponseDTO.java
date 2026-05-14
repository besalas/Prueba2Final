package com.ecommerce.order.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderResponseDTO {
    private Long id;
    private String customerEmail;
    private Long productId;
    private Integer quantity;
    private Double totalPrice;
    private String status;
    private LocalDateTime orderDate;
}
package com.ecommerce.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CartItemRequest(
    @NotNull(message = "El ID del producto es obligatorio")
    Long productId,
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    Integer quantity,
    
    @NotNull(message = "El precio unitario es obligatorio")
    BigDecimal unitPrice
) {}
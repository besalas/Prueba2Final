package com.ecommerce.cart.dto;

import java.math.BigDecimal;

public record CartItemResponse(
    Long productId,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal subTotal
) {}
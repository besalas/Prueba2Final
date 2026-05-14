package com.ecommerce.order.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequestDTO {

    @NotBlank(message = "El correo del cliente es obligatorio")
    @Email(message = "Debe proporcionar un correo válido")
    private String customerEmail;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "Debe pedir al menos 1 unidad")
    private Integer quantity;
}
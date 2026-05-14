package com.ecommerce.recommendation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecommendationRequestDTO {

    @NotBlank(message = "El correo del cliente es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String customerEmail;

    @NotNull(message = "El ID del producto a recomendar es obligatorio")
    private Long productId;
}
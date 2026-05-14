package com.ecommerce.recommendation.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RecommendationResponseDTO {
    private Long id;
    private String customerEmail;
    private Long recommendedProductId;
    private String personalizedMessage;
    private LocalDateTime generatedAt;
}

package com.ecommerce.review.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewResponseDTO {
    private Long id;
    private Long productId;
    private String customerEmail;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}

package com.ecommerce.review.services;

import com.ecommerce.review.dto.ReviewRequestDTO;
import com.ecommerce.review.dto.ReviewResponseDTO;
import com.ecommerce.review.entity.Review;
import com.ecommerce.review.exceptions.ResourceNotFoundException;
import com.ecommerce.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final WebClient.Builder webClientBuilder;

    private final String CATALOG_SERVICE_URL = "http://localhost:8089/api/v1/catalog/products/";

    public ReviewResponseDTO addReview(ReviewRequestDTO requestDTO) {
        log.info("Verificando existencia del producto ID: {} para dejar una reseña", requestDTO.getProductId());

        Boolean productExists = webClientBuilder.build().get()
                .uri(CATALOG_SERVICE_URL + requestDTO.getProductId())
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return Mono.just(true);
                    } else if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.just(false);
                    } else {
                        return response.createException().flatMap(Mono::error);
                    }
                })
                .block();

        if (Boolean.FALSE.equals(productExists)) {
            log.error("Fallo al reseñar: El producto ID {} no existe", requestDTO.getProductId());
            throw new ResourceNotFoundException("No se puede reseñar un producto que no existe en el catálogo.");
        }

        Review review = new Review();
        BeanUtils.copyProperties(requestDTO, review);
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);
        log.info("Reseña guardada con éxito. ID: {}", savedReview.getId());

        return mapToResponseDTO(savedReview);
    }

    public List<ReviewResponseDTO> getReviewsByProduct(Long productId) {
        log.info("Obteniendo todas las reseñas para el producto ID: {}", productId);
        return reviewRepository.findByProductId(productId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private ReviewResponseDTO mapToResponseDTO(Review review) {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        BeanUtils.copyProperties(review, dto);
        return dto;
    }
}

package com.ecommerce.recommendation.services;

import com.ecommerce.recommendation.dto.CatalogProductDTO;
import com.ecommerce.recommendation.dto.RecommendationRequestDTO;
import com.ecommerce.recommendation.dto.RecommendationResponseDTO;
import com.ecommerce.recommendation.entity.Recommendation;
import com.ecommerce.recommendation.exceptions.ResourceNotFoundException;
import com.ecommerce.recommendation.repository.RecommendationRepository;
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
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final WebClient.Builder webClientBuilder;

    private final String CATALOG_SERVICE_URL = "http://localhost:8089/api/v1/catalog/products/";

    public RecommendationResponseDTO generateRecommendation(RecommendationRequestDTO requestDTO) {
        log.info("Generando recomendación para el usuario: {} sobre el producto ID: {}", 
                requestDTO.getCustomerEmail(), requestDTO.getProductId());

        //buscar la información real del producto al catálogo
        CatalogProductDTO productInfo = webClientBuilder.build().get()
                .uri(CATALOG_SERVICE_URL + requestDTO.getProductId())
                .retrieve()
                .onStatus(status -> status.equals(HttpStatus.NOT_FOUND), 
                        response -> Mono.error(new ResourceNotFoundException("El producto ID " + requestDTO.getProductId() + " no existe en el catálogo.")))
                .bodyToMono(CatalogProductDTO.class)
                .block();

        //mensaje personalizado usando los datos del catálogo
        String message = String.format("¡Hola! Basado en tus intereses, te recomendamos el excelente '%s' por solo $%.2f. ¡Mejora tu setup hoy mismo!", 
                productInfo.getName(), productInfo.getPrice());

        // 3. Guardar la recomendación
        Recommendation recommendation = new Recommendation();
        recommendation.setCustomerEmail(requestDTO.getCustomerEmail());
        recommendation.setRecommendedProductId(requestDTO.getProductId());
        recommendation.setPersonalizedMessage(message);
        recommendation.setGeneratedAt(LocalDateTime.now());

        Recommendation savedRecommendation = recommendationRepository.save(recommendation);
        log.info("Recomendación guardada con éxito con ID: {}", savedRecommendation.getId());

        return mapToResponseDTO(savedRecommendation);
    }

    public List<RecommendationResponseDTO> getRecommendationsByEmail(String email) {
        log.info("Buscando recomendaciones para el correo: {}", email);
        return recommendationRepository.findByCustomerEmail(email).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private RecommendationResponseDTO mapToResponseDTO(Recommendation recommendation) {
        RecommendationResponseDTO dto = new RecommendationResponseDTO();
        BeanUtils.copyProperties(recommendation, dto);
        return dto;
    }
}
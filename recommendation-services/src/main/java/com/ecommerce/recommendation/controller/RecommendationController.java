package com.ecommerce.recommendation.controller;

import com.ecommerce.recommendation.dto.RecommendationRequestDTO;
import com.ecommerce.recommendation.dto.RecommendationResponseDTO;
import com.ecommerce.recommendation.services.RecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/generate")
    public ResponseEntity<RecommendationResponseDTO> generateRecommendation(@Valid @RequestBody RecommendationRequestDTO requestDTO) {
        RecommendationResponseDTO response = recommendationService.generateRecommendation(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<List<RecommendationResponseDTO>> getRecommendationsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(recommendationService.getRecommendationsByEmail(email));
    }
}
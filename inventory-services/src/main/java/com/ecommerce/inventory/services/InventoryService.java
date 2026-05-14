package com.ecommerce.inventory.services;

import com.ecommerce.inventory.dto.InventoryRequestDTO;
import com.ecommerce.inventory.dto.InventoryResponseDTO;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.exceptions.ResourceNotFoundException;
import com.ecommerce.inventory.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final WebClient.Builder webClientBuilder;

    private final String CATALOG_SERVICE_URL = "http://localhost:8089/api/v1/catalog/products/";

    public InventoryResponseDTO addInventory(InventoryRequestDTO requestDTO) {
        log.info("Verificando existencia del producto ID: {} en catalog-services", requestDTO.getProductId());

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
                .block(); // .block() lo hace síncrono para este flujo

        if (Boolean.FALSE.equals(productExists)) {
            log.error("Fallo de validación: Producto ID {} no existe en el catálogo", requestDTO.getProductId());
            throw new ResourceNotFoundException("El producto con ID " + requestDTO.getProductId() + " no existe en el catálogo principal");
        }

        log.info("Producto validado correctamente. Procediendo a actualizar inventario.");
        
        Inventory inventory = inventoryRepository
                .findByProductIdAndWarehouseCode(requestDTO.getProductId(), requestDTO.getWarehouseCode())
                .orElse(new Inventory(null, requestDTO.getProductId(), requestDTO.getWarehouseCode(), 0));

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + requestDTO.getAvailableQuantity());
        
        Inventory savedInventory = inventoryRepository.save(inventory);
        log.info("Inventario actualizado para el producto {}. Nueva cantidad: {}", savedInventory.getProductId(), savedInventory.getAvailableQuantity());
        
        return mapToResponseDTO(savedInventory);
    }

    public List<InventoryResponseDTO> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private InventoryResponseDTO mapToResponseDTO(Inventory inventory) {
        InventoryResponseDTO dto = new InventoryResponseDTO();
        BeanUtils.copyProperties(inventory, dto);
        return dto;
    }
}

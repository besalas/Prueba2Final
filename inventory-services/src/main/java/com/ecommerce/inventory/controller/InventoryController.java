package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.InventoryRequestDTO;
import com.ecommerce.inventory.dto.InventoryResponseDTO;
import com.ecommerce.inventory.services.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<InventoryResponseDTO> addInventory(@Valid @RequestBody InventoryRequestDTO requestDTO) {
        InventoryResponseDTO response = inventoryService.addInventory(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }
}
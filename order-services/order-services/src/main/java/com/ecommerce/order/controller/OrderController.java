package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderRequestDTO;
import com.ecommerce.order.dto.OrderResponseDTO;
import com.ecommerce.order.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {
        OrderResponseDTO response = orderService.createOrder(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByEmail(@PathVariable String email) {
        return ResponseEntity.ok(orderService.getOrdersByEmail(email));
    }
}

package com.ecommerce.order.services;

import com.ecommerce.order.dto.CatalogProductDTO;
import com.ecommerce.order.dto.OrderRequestDTO;
import com.ecommerce.order.dto.OrderResponseDTO;
import com.ecommerce.order.entity.HardwareOrder;
import com.ecommerce.order.exceptions.ResourceNotFoundException;
import com.ecommerce.order.repository.OrderRepository;
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
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    private final String CATALOG_SERVICE_URL = "http://localhost:8089/api/v1/catalog/products/";

    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {
        log.info("Iniciando creación de orden para el producto ID: {}", requestDTO.getProductId());

        // información completa del producto desde el catálogo
        CatalogProductDTO catalogProduct = webClientBuilder.build().get()
                .uri(CATALOG_SERVICE_URL + requestDTO.getProductId())
                .retrieve()
                .onStatus(status -> status.equals(HttpStatus.NOT_FOUND), 
                        response -> Mono.error(new ResourceNotFoundException("El producto ID " + requestDTO.getProductId() + " no existe en el catálogo.")))
                .bodyToMono(CatalogProductDTO.class)
                .block();

        // calcular el precio total
        Double calculatedTotal = catalogProduct.getPrice() * requestDTO.getQuantity();
        log.info("Producto validado ({}). Precio unitario: ${}. Total calculado: ${}", 
                catalogProduct.getName(), catalogProduct.getPrice(), calculatedTotal);

        // crear y guardar la orden
        HardwareOrder order = new HardwareOrder();
        BeanUtils.copyProperties(requestDTO, order);
        order.setTotalPrice(calculatedTotal);
        order.setStatus("CREADA");
        order.setOrderDate(LocalDateTime.now());

        HardwareOrder savedOrder = orderRepository.save(order);
        log.info("Orden generada exitosamente con ID: {}", savedOrder.getId());

        return mapToResponseDTO(savedOrder);
    }

    public List<OrderResponseDTO> getOrdersByEmail(String email) {
        log.info("Consultando historial de órdenes para: {}", email);
        return orderRepository.findByCustomerEmail(email).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private OrderResponseDTO mapToResponseDTO(HardwareOrder order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        BeanUtils.copyProperties(order, dto);
        return dto;
    }
}

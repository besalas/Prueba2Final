package com.ecommerce.order.repository;

import com.ecommerce.order.entity.HardwareOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<HardwareOrder, Long> {
    List<HardwareOrder> findByCustomerEmail(String customerEmail);
}
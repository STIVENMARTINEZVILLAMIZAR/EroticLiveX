package com.example.lujuria.commerce.repository;

import com.example.lujuria.commerce.entity.CustomerOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);
}

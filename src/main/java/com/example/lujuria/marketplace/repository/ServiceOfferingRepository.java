package com.example.lujuria.marketplace.repository;

import com.example.lujuria.marketplace.entity.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServiceOfferingRepository
    extends JpaRepository<ServiceOffering, Long>, JpaSpecificationExecutor<ServiceOffering> {
}

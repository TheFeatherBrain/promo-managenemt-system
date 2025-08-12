package com.promo.management.system.promomanagement.repository;

import java.util.UUID;

import com.promo.management.system.promomanagement.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

}

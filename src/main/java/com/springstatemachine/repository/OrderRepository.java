package com.springstatemachine.repository;

import com.springstatemachine.entity.OrderInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderInvoice,Long> {
}

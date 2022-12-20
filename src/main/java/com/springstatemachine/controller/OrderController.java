package com.springstatemachine.controller;

import com.springstatemachine.entity.OrderInvoice;
import com.springstatemachine.enums.OrderStates;
import com.springstatemachine.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderController {
    final OrderRepository orderRepository;

    @PostMapping("/create")
    public OrderInvoice createOrder() {
        OrderInvoice order = new OrderInvoice();
        order.setState(OrderStates.SUBMITTED.name());
        order.setLocalDate(LocalDate.now());
        return orderRepository.save(order);
    }

}

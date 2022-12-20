package com.springstatemachine.service;

import com.springstatemachine.entity.OrderInvoice;
import com.springstatemachine.enums.OrderEvents;
import com.springstatemachine.enums.OrderStates;
import org.springframework.statemachine.StateMachine;

public interface StateFlowService {
    StateMachine<OrderStates, OrderEvents> build(OrderInvoice order);
}

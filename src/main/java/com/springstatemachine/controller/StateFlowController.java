package com.springstatemachine.controller;


import com.springstatemachine.entity.OrderInvoice;
import com.springstatemachine.enums.OrderEvents;
import com.springstatemachine.enums.OrderStates;
import com.springstatemachine.service.StateFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/state")
public class StateFlowController {

    final StateFlowService stateFlowService;

    @PutMapping("/change")
    public String changeState(@RequestBody OrderInvoice order) {

        StateMachine<OrderStates, OrderEvents> sm = stateFlowService.build(order);
        sm.getExtendedState().getVariables().put("paymentType", order.getPaymentType());
        sm.sendEvent(
                MessageBuilder.withPayload(OrderEvents.valueOf(order.getEvent()))
                        .setHeader("orderId", order.getId())
                        .setHeader("state", order.getState())
                        .build()
        );
        return "state changed";
    }
}

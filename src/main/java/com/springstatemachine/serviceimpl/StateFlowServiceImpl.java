package com.springstatemachine.serviceimpl;


import com.springstatemachine.entity.OrderInvoice;
import com.springstatemachine.enums.OrderEvents;
import com.springstatemachine.enums.OrderStates;
import com.springstatemachine.repository.OrderRepository;
import com.springstatemachine.service.StateFlowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StateFlowServiceImpl implements StateFlowService {
    final OrderRepository orderRepository;
    private final StateMachineFactory<OrderStates, OrderEvents> stateMachineFactory;


    @Override
    public StateMachine<OrderStates, OrderEvents> build(final OrderInvoice order) {
        var orderDb = this.orderRepository.findById(order.getId());
        var stateMachine = this.stateMachineFactory.getStateMachine(order.getId().toString());
        stateMachine.stop();
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(new StateMachineInterceptorAdapter<OrderStates, OrderEvents>() {
                        @Override
                        public void preStateChange(State<OrderStates, OrderEvents> state, Message<OrderEvents> message, Transition<OrderStates, OrderEvents> transition, StateMachine<OrderStates, OrderEvents> stateMachine, StateMachine<OrderStates, OrderEvents> rootStateMachine) {
                            var orderId = Long.class.cast(message.getHeaders().get("orderId"));
                            var order = orderRepository.findById(orderId);
                            if (order.isPresent()) {
                                order.get().setState(state.getId().name());
                                orderRepository.save(order.get());
                            }
                        }
                    });
                    sma.resetStateMachine(new DefaultStateMachineContext<>(OrderStates.valueOf(orderDb.get().getState()), null, null, null));
                });
        stateMachine.start();
        return stateMachine;
    }
}

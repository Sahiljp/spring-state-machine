package com.springstatemachine.config;

import com.springstatemachine.enums.OrderEvents;
import com.springstatemachine.enums.OrderStates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.util.StringUtils;


@Configuration
@EnableStateMachineFactory
@Slf4j
public class SpringStateMachineConfig extends StateMachineConfigurerAdapter<OrderStates, OrderEvents> {


    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
        states.withStates()
                .initial(OrderStates.SUBMITTED)
                .state(OrderStates.PAID)
                .end(OrderStates.FULFILLED)
                .end(OrderStates.CANCELED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates,
            OrderEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderStates.SUBMITTED)
                .target(OrderStates.PAID)
                .event(OrderEvents.PAY)
                .guard(guard())

                .and()
                .withExternal()
                .source(OrderStates.PAID)
                .target(OrderStates.FULFILLED)
                .event(OrderEvents.FULFILL)
                .action(ctx -> {
                    log.info("This PAID handler where we can perform some logging");
                })

                .and()
                .withExternal()
                .source(OrderStates.SUBMITTED)
                .target(OrderStates.CANCELED)
                .event(OrderEvents.CANCEL)
                .action(ctx -> {
                    log.info("This SUBMITTED handler where we can perform some logging");
                })

                .and()
                .withExternal()
                .source(OrderStates.PAID)
                .target(OrderStates.CANCELED)
                .event(OrderEvents.CANCEL)
                .action(ctx -> {
                    log.info("This PAID handler where we can perform some logging");
                });

    }

    @Bean
    public Guard<OrderStates, OrderEvents> guard() {
        return ctx -> {
            String paymentType = String.class.cast(ctx.getExtendedState()
                    .getVariables().get("paymentType"));
            if (!StringUtils.isEmpty(paymentType) && paymentType.equals("cod"))
                return false;
            else return true;
        };
    }

}

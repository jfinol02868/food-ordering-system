package com.food.ordering.system.order.service.test;

import com.food.ordering.system.order.service.test.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.test.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.test.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.test.port.output.message.publisher.payment.OrderCreatePaymentRequestMessagePublisher;
import com.food.ordering.system.service.domain.event.OrderCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderCreateHelper orderCreateHelper;
    private final OrderDataMapper orderDataMapper;

    private final OrderCreatePaymentRequestMessagePublisher orderCreatePaymentRequestMessagePublisher;

    public OrderCreateCommandHandler(
            OrderCreateHelper orderCreateHelper,
            OrderDataMapper orderDataMapper, OrderCreatePaymentRequestMessagePublisher orderCreatePaymentRequestMessagePublisher) {
        this.orderCreateHelper = orderCreateHelper;
        this.orderDataMapper = orderDataMapper;
        this.orderCreatePaymentRequestMessagePublisher = orderCreatePaymentRequestMessagePublisher;
    }

    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        OrderCreateEvent orderCreateEvent = orderCreateHelper.persistOrder(createOrderCommand);
        log.info("Order is created with id {} ", orderCreateEvent.getOrder().getId().getValue());
        orderCreatePaymentRequestMessagePublisher.publish(orderCreateEvent);
        return orderDataMapper.orderToCreateOrderResponse(orderCreateEvent.getOrder(), "OrderCreated ");
    }
}

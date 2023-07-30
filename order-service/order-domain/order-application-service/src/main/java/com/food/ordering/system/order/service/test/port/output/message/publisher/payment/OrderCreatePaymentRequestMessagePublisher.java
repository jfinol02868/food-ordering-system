package com.food.ordering.system.order.service.test.port.output.message.publisher.payment;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.service.domain.event.OrderCreateEvent;

public interface OrderCreatePaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreateEvent> {
}

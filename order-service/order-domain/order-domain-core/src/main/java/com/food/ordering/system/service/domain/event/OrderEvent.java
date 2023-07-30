package com.food.ordering.system.service.domain.event;

import com.food.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.OrderItem;
import lombok.Getter;

import java.time.ZonedDateTime;

public abstract class OrderEvent implements DomainEvent<Order> {

    @Getter
    private final Order order;
    private final ZonedDateTime createAt;

    public OrderEvent(Order order, ZonedDateTime createAt) {
        this.order = order;
        this.createAt = createAt;
    }

    public Order getOrder() {
        return order;
    }

    public ZonedDateTime getCreateAt() {
        return createAt;
    }
}

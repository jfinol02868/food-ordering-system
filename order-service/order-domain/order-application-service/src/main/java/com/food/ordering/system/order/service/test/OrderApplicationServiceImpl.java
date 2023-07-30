package com.food.ordering.system.order.service.test;

import com.food.ordering.system.order.service.test.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.test.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.test.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.test.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.test.port.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler orderCreateCommandHandler;
    private final OrderTrackCommandHandler orderTrackCommandHandler;

    public OrderApplicationServiceImpl(OrderTrackCommandHandler orderTrackCommandHandler, OrderCreateCommandHandler orderCreateCommandHandler) {
        this.orderTrackCommandHandler = orderTrackCommandHandler;
        this.orderCreateCommandHandler = orderCreateCommandHandler;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return orderCreateCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return orderTrackCommandHandler.trackOrder(trackOrderQuery);
    }
}

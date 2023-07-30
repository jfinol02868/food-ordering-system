package com.food.ordering.system.order.service.test.port.input.service;

import com.food.ordering.system.order.service.test.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.test.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.test.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.test.dto.track.TrackOrderResponse;
import jakarta.validation.Valid;

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}

package com.food.ordering.system.order.service.test;

import com.food.ordering.system.order.service.test.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.test.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.test.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.test.port.output.repository.OrderRepository;
import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.service.domain.valueobject.TrackingId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;

    private final OrderRepository orderRepository;

    public OrderTrackCommandHandler(
            OrderDataMapper orderDataMapper,
            OrderRepository orderRepository) {
        this.orderDataMapper = orderDataMapper;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        Optional<Order>  optionalOrder = orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));
       if(optionalOrder.isEmpty()) {
           log.info("The order with trackId {} not exist.", trackOrderQuery.getOrderTrackingId());
           throw new OrderNotFoundException("The order with trackId "+ trackOrderQuery.getOrderTrackingId()+" not exist.");
       }
        return orderDataMapper.orderToTrackOrderResponse(optionalOrder.get());
    }
}

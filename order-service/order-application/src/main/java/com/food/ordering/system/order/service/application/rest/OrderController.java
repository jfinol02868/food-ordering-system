package com.food.ordering.system.order.service.application.rest;

import com.food.ordering.system.order.service.test.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.test.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.test.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.test.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.test.port.input.service.OrderApplicationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/order", produces = "application/vnd.api.v1+json")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand createOrderCommand) {
        log.info("Create order for customer: {} at restaurant: {}", createOrderCommand.getCustomerId(), createOrderCommand.getRestaurantId());
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        log.info("Order created with trackingId: {}");
        return new ResponseEntity<>(createOrderResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(@PathVariable UUID trackingId) {
        TrackOrderResponse trackOrderResponse = orderApplicationService
                .trackOrder(TrackOrderQuery.builder().orderTrackingId(trackingId).build());
        log.info("Returning order with trackingId: {}", trackingId);
        return ResponseEntity.ok(trackOrderResponse);
    }
}

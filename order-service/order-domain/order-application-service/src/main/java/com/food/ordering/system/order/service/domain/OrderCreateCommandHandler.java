package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.port.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.port.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.port.output.repository.RestaurantRepository;
import com.food.ordering.system.service.domain.OrderDomainService;
import com.food.ordering.system.service.domain.entity.Customer;
import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.Restaurant;
import com.food.ordering.system.service.domain.event.OrderCreateEvent;
import com.food.ordering.system.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderDomainService orderDomainService;
    private final RestaurantRepository  restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateCommandHandler(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            OrderDomainService orderDomainService,
            RestaurantRepository restaurantRepository,
            OrderDataMapper orderDataMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderDomainService = orderDomainService;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant =  checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreateEvent orderCreateEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        Order orderResult = this.saveOrder(order);
        log.info("Order is created with id {} ", orderResult.getId().getValue());
        return orderDataMapper.orderToCreateOrderResponse(orderResult);
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInformation(restaurant);
        if(optionalRestaurant.isEmpty()) {
            log.info("The restaurant with id {} no exist.", createOrderCommand.getRestaurantId());
            throw new OrderDomainException("The restaurant with id "+createOrderCommand.getRestaurantId()+" no exist.");
        }
        return optionalRestaurant.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if(customer.isEmpty()) {
            log.info("The costumer with id {} not exist.");
            throw new OrderDomainException("The customer with id: "+customerId+" not exist");
        }
    }

    private Order saveOrder(Order order) {
        Order orderResult = orderRepository.save(order);
        if(Objects.isNull(orderResult)) {
            log.info("The order whit id {} is not saved.", orderResult.getId().getValue());
            throw new OrderDomainException("he order whit id "+orderResult.getId().getValue()+" in not saved.");
        }
        log.info("The order whit id {} is save.", orderResult.getId().getValue());
        return orderResult;
    }
}

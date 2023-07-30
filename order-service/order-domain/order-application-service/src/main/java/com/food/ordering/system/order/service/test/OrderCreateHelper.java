package com.food.ordering.system.order.service.test;

import com.food.ordering.system.order.service.test.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.test.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.test.port.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.test.port.output.repository.OrderRepository;
import com.food.ordering.system.order.service.test.port.output.repository.RestaurantRepository;
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
public class OrderCreateHelper {

       private final OrderDomainService orderDomainService;
       private final OrderRepository orderRepository;
       private final CustomerRepository customerRepository;
       private final RestaurantRepository restaurantRepository;
       private final OrderDataMapper orderDataMapper;

    public OrderCreateHelper(OrderDomainService orderDomainService,
                             OrderRepository orderRepository,
                             CustomerRepository customerRepository,
                             RestaurantRepository restaurantRepository,
                             OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public OrderCreateEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant =  checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreateEvent orderCreateEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        this.saveOrder(order);
        log.info("Order is created with id {} ", order.getId().getValue());
        return orderCreateEvent;
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
            log.info("The order whit id {} is not saved.", order.getId().getValue());
            throw new OrderDomainException("he order whit id "+order.getId().getValue()+" in not saved.");
        }
        log.info("The order whit id {} is save.", order.getId().getValue());
        return orderResult;
    }
}

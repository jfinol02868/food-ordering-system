package com.food.ordering.system.service.domain;

import com.food.ordering.system.domain.exception.DomainException;
import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.Product;
import com.food.ordering.system.service.domain.entity.Restaurant;
import com.food.ordering.system.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.service.domain.event.OrderCreateEvent;
import com.food.ordering.system.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    private final static String UTC = "UTC";

    @Override
    public OrderCreateEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initiate.", order.getId().getValue());
        return new OrderCreateEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id {} is pay.", order.getId().getValue());
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        order.approved();
        log.info("Order with id {} is approved.", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order with id {} is cancelling.", order.getId().getValue());
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id {} is cancel.", order.getId().getValue());
    }

    private void validateRestaurant(Restaurant restaurant) {
        if(!restaurant.isActive()) {
            log.info("The restaurant with id {} is not active.", restaurant.getId().getValue());
            throw new OrderDomainException("The restaurant with ID:"+restaurant.getId().getValue()+" is not active.");
        }
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        order.getItems().forEach( orderItem -> restaurant.getProducts().forEach(restaurantProduct -> {
            Product currentProduct = orderItem.getProduct();
            if(currentProduct.equals(restaurantProduct)) {
                currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
            }
        }));
    }

    private void setOrderProductInformation2(Order order, Restaurant restaurant) {
        //Versión optimizada con mapas del metodo de arriba
        Map<String, Product> productMap = new HashMap<>();
        for (Product restaurantProduct : restaurant.getProducts()) {
            productMap.put(restaurantProduct.getName(), restaurantProduct);
        }

        order.getItems().forEach(orderItem -> {
            Product currentProduct = orderItem.getProduct();
            Product restaurantProduct = productMap.get(currentProduct.getName());
            if (restaurantProduct != null) {
                currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
            }
        });
    }
}

package com.food.ordering.system.order.service.dataaccess.order.mapper;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderEntity;
import com.food.ordering.system.order.service.dataaccess.order.entity.OrderItemEntity;
import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.OrderItem;
import com.food.ordering.system.service.domain.entity.Product;
import com.food.ordering.system.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.food.ordering.system.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Component
public class OrderDataAccessMapper {

    public OrderEntity orderToOrderEntity(Order order){
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(order.getTrackingId().getValue())
                .orderAddressEntity(deliveryOrderAddressEntity(order.getDeliveryAddress()))
                .price(order.getPrice().getAmount())
                .items(orderItemToOrderEntities(order.getItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessage(order.getFailureMessages() != null ? String.join(FAILURE_MESSAGE_DELIMITER, order.getFailureMessages()) : "")
                .build();
        orderEntity.getOrderAddressEntity().setOrder(orderEntity);
        orderEntity.getItems().forEach( orderItemEntity -> orderItemEntity.setOrder(orderEntity));

        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        Order order = Order.builder()
                .orderId(new OrderId(orderEntity.getId()))
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .deliveryAddress(addressEntityToDeliveryAddress(orderEntity.getOrderAddressEntity()))
                .price(new Money(orderEntity.getPrice()))
                .items(orderItemEntityToOrderItem(orderEntity.getItems()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessages(orderEntity.getFailureMessage().isEmpty() ? new ArrayList<>() :
                        new ArrayList<>(Arrays.asList(orderEntity.getFailureMessage().split(FAILURE_MESSAGE_DELIMITER))))
                .build();
        return order;
    }

    private List<OrderItem> orderItemEntityToOrderItem(List<OrderItemEntity> items) {
            return items.stream().map( orderItemEntity -> OrderItem.builder()
                    .orderItemId(new OrderItemId(orderItemEntity.getId()))
                    .product(new Product(new ProductId(orderItemEntity.getProductId())))
                    .price(new Money(orderItemEntity.getPrice()))
                    .quantity(orderItemEntity.getQuantity())
                    .subTotal(new Money(orderItemEntity.getSubTotal()))
                    .build()).collect(Collectors.toList());
    }

    private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity orderAddressEntity) {
        return new StreetAddress(orderAddressEntity.getId(),
                orderAddressEntity.getStreet(),
                orderAddressEntity.getPostalCode(),
                orderAddressEntity.getCity()
        );
    }




    private List<OrderItemEntity> orderItemToOrderEntities(List<OrderItem> items) {
        return items.stream().map( orderItem -> OrderItemEntity.builder()
                .id(orderItem.getId().getValue())
                .productId(orderItem.getProduct().getId().getValue())
                .price(orderItem.getPrice().getAmount())
                .quantity(orderItem.getQuantity())
                .subTotal(orderItem.getSubTotal().getAmount())
                .build()).collect(Collectors.toList());
    }

    private OrderAddressEntity deliveryOrderAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .id(deliveryAddress.getId())
                .street(deliveryAddress.getStreet())
                .city(deliveryAddress.getCity())
                .postalCode(deliveryAddress.getPostalCode())
                .build();
    }


}

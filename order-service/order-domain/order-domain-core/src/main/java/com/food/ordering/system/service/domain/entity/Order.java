package com.food.ordering.system.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.service.domain.exception.OrderDomainException;
import com.food.ordering.system.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.service.domain.valueobject.TrackingId;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> items;
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public void validateOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItem();
    }

    private void initializeOrderItem() {
        long itemId = 1;
        for (OrderItem orderItem: items) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId ++));
        }
    }

    public void initializeOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemPrice();
        if(!Objects.equals(orderStatus, OrderStatus.PENDING)) {

        }
    }

    private void validateInitialOrder() {
        if(Objects.nonNull(orderStatus) || Objects.nonNull(getId())) {
            throw new OrderDomainException("Order is not in the correct status for initialization.");
        }
    }

    private void validateTotalPrice() {
        if (Objects.isNull(this.price) || this.price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero.");
        }
    }

    private void validateItemPrice() {
        Money orderItemsTotal = items.stream().map( orderItem -> {
            validateItemsPrice(orderItem);
            return orderItem.getSubTotal();
        }).reduce(Money.ZERO, Money::add);

        if(!this.price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: "+ price.getAmount()+ " is not equal to order items total: "+ orderItemsTotal);
        }
    }

    private void validateItemsPrice(OrderItem orderItem) {
        if(!orderItem.isPriceValid()) {
            throw new OrderDomainException("Order items price "+orderItem.getPrice().getAmount()+ " is no valid for product "+ orderItem.getProduct().getId().getValue());
        }
    }

    public void pay() {
        if(!Objects.equals(this.orderStatus, OrderStatus.PENDING) ) {
            throw new OrderDomainException("Order is not in correct status for pay operation.");
        }
        this.orderStatus = OrderStatus.PAID;
    }

    public void approved() {
        if(!Objects.equals(this.orderStatus, OrderStatus.PAID) ) {
            throw new OrderDomainException("Order is not in correct status for approved operation.");
        }
        this.orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages) {
        if(!Objects.equals(this.orderStatus, OrderStatus.PAID) ) {
            throw new OrderDomainException("Order is not in correct status for cancelling operation.");
        }
        this.orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(List<String> failureMessages) {
        if(!(Objects.equals(this.orderStatus, OrderStatus.PENDING) ||  Objects.equals(this.orderStatus, OrderStatus.CANCELLING))) {
            throw new OrderDomainException("Order is not in correct status for cancel operation.");
        }
        this.orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if(this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter( message -> !message.isEmpty()).toList());
        }
        if(this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public Builder orderId(OrderId orderId) {
            orderId = orderId;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}

package com.food.ordering.system.order.service.test.port.input.message.listener.restaurantapproval;

import com.food.ordering.system.order.service.test.dto.messsage.RestaurantApprovalResponse;

public interface RestaurantApprovalMessageListener {

    void orderApproval(RestaurantApprovalResponse restaurantApprovalResponse);

    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}

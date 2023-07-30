package com.food.ordering.system.order.service.domain.port.input.message.listener.payment;

import com.food.ordering.system.order.service.domain.dto.messsage.PaymentResponse;

public interface PaymentResponseMessageListener {

    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCancelled(PaymentResponse paymentResponse);
}

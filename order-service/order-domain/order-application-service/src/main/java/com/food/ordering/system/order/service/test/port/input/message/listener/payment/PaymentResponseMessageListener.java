package com.food.ordering.system.order.service.test.port.input.message.listener.payment;

import com.food.ordering.system.order.service.test.dto.messsage.PaymentResponse;

public interface PaymentResponseMessageListener {

    void paymentCompleted(PaymentResponse paymentResponse);

    void paymentCancelled(PaymentResponse paymentResponse);
}

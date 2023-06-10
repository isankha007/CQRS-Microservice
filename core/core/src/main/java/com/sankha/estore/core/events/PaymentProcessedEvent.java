package com.sankha.estore.core.events;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
public class PaymentProcessedEvent {
    private final String orderId;
    private final String paymentId;
}

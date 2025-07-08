package my.payment_pagpay.infrastructure.kafka;

import my.payment_pagpay.domain.dto.PaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, PaymentDto> kafkaTemplate;

    private final String topic = "payment-topic";

    public void paymentProducer(PaymentDto paymentDto) {
        kafkaTemplate.send(topic, paymentDto);
    }
}

package my.payment_pagsafe.infrastructure.kafka;

import my.payment_pagsafe.domain.dto.PaymentDto;
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

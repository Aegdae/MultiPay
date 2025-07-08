package my.payment_process.services;

import my.payment_process.domain.dto.PaymentDto;
import my.payment_process.domain.entity.Payment;
import my.payment_process.infrastructure.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentReceivedRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public void processPayment(PaymentDto paymentDto) {
        PaymentDto savedDto = savePayments(paymentDto);
        int MAX_RETRY = 3;
        for(int RETRY = 0; RETRY <= MAX_RETRY; RETRY++) {
            try {

                restTemplate.postForObject("http://payment-pagpay:8001/payments", savedDto, Void.class);
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        for(int RETRY = 0; RETRY <= MAX_RETRY; RETRY++) {
            try {
                restTemplate.postForObject("http://payment-pagpay:8002/payments", savedDto, Void.class);
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("Erro ao processar pagamentos");
    }

    public PaymentDto savePayments(PaymentDto paymentDto) {
        Payment payment = new Payment();
        payment.setUsername(paymentDto.getUsername());
        payment.setAmount(paymentDto.getAmount());

        Payment savedPayment = paymentReceivedRepository.save(payment);
        paymentDto.setId(savedPayment.getId());

        return paymentDto;
    }

    @KafkaListener(topics = "payment-topic", groupId = "payment-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumerPayment(PaymentDto paymentDto){
        Payment payment = new Payment();
        payment.setUsername(paymentDto.getUsername());
        payment.setName(paymentDto.getName());
        payment.setAmount(paymentDto.getAmount());
        payment.setTax(paymentDto.getTax());
        payment.setTotal(paymentDto.getTotal());
        payment.setStatus(paymentDto.getStatus());

        paymentReceivedRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentReceivedRepository.findAll();
    }

    public List<Payment> getAllPaymentsForId(String username) {
        return paymentReceivedRepository.findAllByUsername(username);

    }
}

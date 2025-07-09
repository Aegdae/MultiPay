package my.payment_process.services;

import my.payment_process.domain.dto.PaymentDto;
import my.payment_process.domain.entity.Payment;
import my.payment_process.domain.entity.PaymentStatus;
import my.payment_process.infrastructure.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public void processPayment(PaymentDto paymentDto) {
        PaymentDto savedDto = new PaymentDto();
        savedDto.setUsername(paymentDto.getUsername());
        savedDto.setMethods(paymentDto.getMethods());
        savedDto.setAmount(paymentDto.getAmount());
        savedDto.setStatus(PaymentStatus.PENDING);
        savePayments(savedDto);

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
        payment.setMethods(paymentDto.getMethods());
        payment.setAmount(paymentDto.getAmount());
        payment.setStatus(paymentDto.getStatus());

        Payment savedPayment = paymentRepository.save(payment);
        paymentDto.setProcessId(savedPayment.getProcessId());

        return paymentDto;
    }

    @KafkaListener(topics = "payment-topic", groupId = "payment-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumerPayment(PaymentDto paymentDto){
        Payment payment = new Payment();
        payment.setUsername(paymentDto.getUsername());
        payment.setName(paymentDto.getName());
        payment.setMethods(paymentDto.getMethods());
        payment.setAmount(paymentDto.getAmount());
        payment.setTax(paymentDto.getTax());
        payment.setTotal(paymentDto.getTotal());
        payment.setStatus(paymentDto.getStatus());

        paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getAllPaymentsForId(String username) {
        return paymentRepository.findAllByUsername(username);

    }

    public void deletAll() {
        paymentRepository.deleteAll();
    }
}

package my.payment_process.services;

import my.payment_process.domain.dto.PaymentDto;
import my.payment_process.domain.entity.Payment;
import my.payment_process.domain.entity.PaymentStatus;
import my.payment_process.infrastructure.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * Processa o pagamento enviando os dados para um dos gateways disponíveis (PagPay ou Pagsafe).
     * Salva o pagamento inicialmente com status PENDING e tenta o envio com retries.
     * Retorna 200 em caso de sucesso ou 502 em caso de falha em ambos os gateways.
     */
    @Transactional
    public ResponseEntity<Void> processPayment(PaymentDto paymentDto) {
        PaymentDto savedDto = new PaymentDto();
        savedDto.setUsername(paymentDto.getUsername());
        savedDto.setMethods(paymentDto.getMethods());
        savedDto.setAmount(paymentDto.getAmount());
        savedDto.setStatus(PaymentStatus.PENDING);
        savedDto = savePayments(savedDto);

        int MAX_RETRY = 3;
        for(int RETRY = 1; RETRY <= MAX_RETRY; RETRY++) {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity("http://payment-pagpay:8001/pagpay/payments", savedDto, String.class);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                try { Thread.sleep(1000); } catch (InterruptedException ignored){}
            }
        }

        // Tentativa de pagamento no Pagsafe
        for(int RETRY = 1; RETRY <= MAX_RETRY; RETRY++) {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity("http://payment-pagsafe:8002/pagsafe/payments", savedDto, String.class);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                try { Thread.sleep(150); } catch (InterruptedException ignored){}
            }
        }

        // Todas tentativas falharam
        return ResponseEntity.status(502).build();
    }

    /**
     * Salva os dados do pagamento no banco de dados com status atual.
     */
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

    /**
     * Consome os dados enviados pelos gateways via Kafka e atualiza os dados do pagamento.
     */
    @KafkaListener(topics = "payment-topic", groupId = "payment-consumer-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumerPayment(PaymentDto paymentDto){
        Payment payment = new Payment();
        payment.setProcessId(paymentDto.getProcessId());
        payment.setUsername(paymentDto.getUsername());
        payment.setName(paymentDto.getName());
        payment.setMethods(paymentDto.getMethods());
        payment.setAmount(paymentDto.getAmount());
        payment.setTax(paymentDto.getTax());
        payment.setTotal(paymentDto.getTotal());
        payment.setStatus(paymentDto.getStatus());

        paymentRepository.save(payment);
    }

    /**
     * Retorna todos os pagamentos registrados.
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    /**
     * Retorna todos os pagamentos de um usuário específico.
     */
    public List<Payment> getAllPaymentsForId(String username) {
        return paymentRepository.findAllByUsername(username);

    }

    /**
     * Deleta todos os registros de pagamento do banco.
     */
    public void deletAll() {
        paymentRepository.deleteAll();
    }
}

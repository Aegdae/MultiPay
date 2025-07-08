package my.payment_process.services;

import my.payment_process.domain.dto.PaymentDto;
import my.payment_process.domain.dto.PaymentReceivedDto;
import my.payment_process.domain.entity.PaymentReceived;
import my.payment_process.domain.entity.PaymentStatus;
import my.payment_process.infrastructure.repository.PaymentReceivedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentReceivedRepository paymentReceivedRepository;

    @Autowired
    private RestTemplate restTemplate;

    public void processPayment(PaymentDto paymentDto) {

        int MAX_RETRY = 3;
        for(int RETRY = 0; RETRY <= MAX_RETRY; RETRY++) {
            try {

                restTemplate.postForObject("http://payment-pagpay:8001/payments", paymentDto, Void.class);
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        for(int RETRY = 0; RETRY <= MAX_RETRY; RETRY++) {
            try {
                restTemplate.postForObject("http://payment-pagpay:8002/payments", paymentDto, Void.class);
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("Erro ao processar pagamentos");
    }

    public List<PaymentReceived> getAllPayments() {
        return paymentReceivedRepository.findAll();
    }

    public PaymentReceived savePayments(PaymentReceivedDto paymentReceivedDto) {
        PaymentReceived paymentSaved = new PaymentReceived();
        paymentSaved.setId(paymentReceivedDto.getId());
        paymentSaved.setUsername(paymentReceivedDto.getUsername());
        paymentSaved.setName(paymentReceivedDto.getName());
        paymentSaved.setAmount(paymentReceivedDto.getAmount());
        paymentSaved.setTax(paymentReceivedDto.getTax());
        paymentSaved.setTotal(paymentReceivedDto.getTotal());

        return paymentReceivedRepository.save(paymentSaved);
    }
}

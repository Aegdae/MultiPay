package my.payment_process.api.payments;

import my.payment_process.domain.dto.PaymentDto;
import my.payment_process.domain.dto.PaymentReceivedDto;
import my.payment_process.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payments")
    private ResponseEntity<Void> processPayment(PaymentDto paymentDto){
        paymentService.processPayment(paymentDto);
        return ResponseEntity.accepted().build();
    }
}

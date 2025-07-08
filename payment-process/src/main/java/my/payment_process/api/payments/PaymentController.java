package my.payment_process.api.payments;

import my.payment_process.domain.dto.PaymentDto;
import my.payment_process.domain.entity.Payment;
import my.payment_process.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process-payments")
    public ResponseEntity<Void> processPayment(@RequestBody PaymentDto paymentDto){
        paymentService.processPayment(paymentDto);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/get-payments")
    public ResponseEntity<List<Payment>> getAllPayments(){
        List<Payment> listPayements = paymentService.getAllPayments();
        return ResponseEntity.ok(listPayements);
    }
}

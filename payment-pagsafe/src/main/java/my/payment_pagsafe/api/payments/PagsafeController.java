package my.payment_pagsafe.api.payments;

import my.payment_pagsafe.domain.dto.PaymentDto;
import my.payment_pagsafe.domain.entity.Pagsafe;
import my.payment_pagsafe.services.PagsafeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagsafe")
public class PagsafeController {

    @Autowired
    private PagsafeService pagsafeService;

    @PostMapping("/payments")
    public ResponseEntity<Void> payments(@RequestBody PaymentDto paymentDto) {
        pagsafeService.payment(paymentDto);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/get-payments")
    public ResponseEntity<List<Pagsafe>> getAllPayments() {
        List<Pagsafe> allPayments = pagsafeService.getAllPayments();
        return ResponseEntity.ok(allPayments);
    }

    @DeleteMapping("/delete-payments")
    public ResponseEntity<?> deleteAll() {
        pagsafeService.deletAll();
        return ResponseEntity.noContent().build();
    }
}

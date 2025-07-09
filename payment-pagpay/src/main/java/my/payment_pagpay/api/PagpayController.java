package my.payment_pagpay.api;

import my.payment_pagpay.domain.dto.PaymentDto;
import my.payment_pagpay.domain.entity.Pagpay;
import my.payment_pagpay.services.PagpayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagpay")
public class PagpayController {

    @Autowired
    private PagpayService pagpayService;

    @PostMapping("/payments")
    public ResponseEntity<Void> payments(@RequestBody PaymentDto paymentDto) {
        System.out.println("Recebido no Pagpay: " + paymentDto);
        pagpayService.payment(paymentDto);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/get-payments")
    public ResponseEntity<List<Pagpay>> getAllPayments() {
        List<Pagpay> allPayments = pagpayService.getAllPayments();
        return ResponseEntity.ok(allPayments);
    }

    @DeleteMapping("delete-payments")
    public ResponseEntity<?> deleteAll() {
        pagpayService.deletAll();
        return ResponseEntity.noContent().build();
    }
}

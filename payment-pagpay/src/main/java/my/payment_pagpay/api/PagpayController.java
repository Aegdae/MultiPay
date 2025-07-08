package my.payment_pagpay.api;

import my.payment_pagpay.domain.dto.PaymentDto;
import my.payment_pagpay.services.PagpayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PagpayController {

    private PagpayService pagpayService;

    @PostMapping
    public ResponseEntity<Void> payments(@RequestBody PaymentDto paymentDto) {
        pagpayService.payment(paymentDto);
        return ResponseEntity.accepted().build();
    }
}

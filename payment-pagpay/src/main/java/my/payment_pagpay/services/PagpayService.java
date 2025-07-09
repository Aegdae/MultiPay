package my.payment_pagpay.services;

import my.payment_pagpay.domain.dto.PaymentDto;
import my.payment_pagpay.domain.entity.Pagpay;
import my.payment_pagpay.domain.entity.PagpayStatus;
import my.payment_pagpay.infrastructure.kafka.KafkaProducer;
import my.payment_pagpay.infrastructure.repository.PagpayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class PagpayService {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private PagpayRepository pagpayRepository;

    public Pagpay payment(PaymentDto paymentDto) {
        if (paymentDto.getProcessId() == null) {
            throw new IllegalArgumentException("ProcessId não pode ser nulo");
        }

        double taxD = 0.04;
        BigDecimal taxBD = BigDecimal.valueOf(taxD);
        BigDecimal tax = paymentDto.getAmount()
                .multiply(taxBD)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = paymentDto.getAmount()
                .subtract(tax)
                .setScale(2, RoundingMode.HALF_UP);


        Pagpay pagpay = new Pagpay();
        pagpay.setProcessId(paymentDto.getProcessId());
        pagpay.setUsername(paymentDto.getUsername());
        pagpay.setName("PagPay");
        pagpay.setMethods(paymentDto.getMethods());
        pagpay.setAmount(paymentDto.getAmount());
        pagpay.setTax(tax);
        pagpay.setTotal(total);
        pagpay.setStatus(PagpayStatus.SUCCESS);

        PaymentDto dto = new PaymentDto();
        dto.setProcessId(pagpay.getId());
        dto.setUsername(pagpay.getUsername());
        dto.setName(pagpay.getName());
        dto.setMethods(pagpay.getMethods());
        dto.setAmount(pagpay.getAmount());
        dto.setTax(pagpay.getTax());
        dto.setTotal(pagpay.getTotal());
        dto.setStatus(PagpayStatus.SUCCESS);

        kafkaProducer.paymentProducer(dto);
        return pagpayRepository.save(pagpay);
    }

    public List<Pagpay> getAllPayments() {
        List<Pagpay> allPayments = pagpayRepository.findAll();
        return allPayments;
    }

    public void deletAll() {
        pagpayRepository.deleteAll();
    }
}

package my.payment_pagpay.domain.dto;

import lombok.Data;
import my.payment_pagpay.domain.entity.PagpayStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentDto {
    private UUID processId;
    private String username;
    private String name;
    private BigDecimal amount;
    private BigDecimal tax;
    private BigDecimal total;
    private PagpayStatus status;
}

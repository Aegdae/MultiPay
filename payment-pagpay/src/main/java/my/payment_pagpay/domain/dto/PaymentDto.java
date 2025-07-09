package my.payment_pagpay.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import my.payment_pagpay.domain.entity.PagpayStatus;
import my.payment_pagpay.domain.entity.PaymentMethods;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentDto {
    private UUID processId;
    private String username;
    private String name;
    private PaymentMethods methods;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal amount;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal tax;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal total;
    private PagpayStatus status;
}

package my.payment_process.domain.dto;

import lombok.Data;
import my.payment_process.domain.entity.PaymentStatus;

import java.math.BigDecimal;

@Data
public class PaymentDto {
    private String username;
    private BigDecimal amount;
    private PaymentStatus status = PaymentStatus.PENDING;
}

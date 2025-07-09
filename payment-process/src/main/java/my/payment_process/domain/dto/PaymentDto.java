package my.payment_process.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import my.payment_process.domain.entity.PaymentMethods;
import my.payment_process.domain.entity.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentDto {
    private UUID processId;
    private String username;
    private String name;
    private PaymentMethods methods;
    private BigDecimal amount;
    private BigDecimal tax;
    private BigDecimal total;
    private PaymentStatus status;
}

package my.payment_pagsafe.domain.dto;

import lombok.Data;
import my.payment_pagsafe.domain.entity.PagsafeStatus;
import my.payment_pagsafe.domain.entity.PaymentMethods;

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
    private PagsafeStatus status;
}

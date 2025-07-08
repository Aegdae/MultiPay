package my.payment_process.domain.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import my.payment_process.domain.entity.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Getter
@Setter
public class PaymentReceivedDto {

    private UUID id;
    private String username;
    private String name;
    private BigDecimal amount;
    private BigDecimal tax;
    private BigDecimal total;
    private PaymentStatus status;

}

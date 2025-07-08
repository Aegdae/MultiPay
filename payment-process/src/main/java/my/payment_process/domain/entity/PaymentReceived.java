package my.payment_process.domain.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
public class PaymentReceived {
    private UUID id;
    private String username;
    private String name;
    private BigDecimal amount;
    private BigDecimal tax;
    private BigDecimal total;
    private PaymentStatus status;
}

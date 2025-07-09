package my.payment_process.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID processId;
    private String username;
    private String name;
    private PaymentMethods methods;
    private BigDecimal amount;
    private BigDecimal tax;
    private BigDecimal total;
    private PaymentStatus status;
}

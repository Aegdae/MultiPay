package my.payment_pagsafe.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Pagsafe {

    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private UUID eventId;
    @Column(unique = true)
    private UUID processId;
    private String username;
    private String name;
    private PaymentMethods methods;
    private BigDecimal amount;
    private BigDecimal tax;
    private BigDecimal total;
    private PagsafeStatus status;
    @CreationTimestamp
    private Instant createdAt;
}

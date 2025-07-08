package my.payment_process.infrastructure.repository;

import my.payment_process.domain.entity.PaymentReceived;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentReceivedRepository extends JpaRepository<PaymentReceived, UUID> {
}

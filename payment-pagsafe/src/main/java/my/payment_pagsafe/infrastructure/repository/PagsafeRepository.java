package my.payment_pagsafe.infrastructure.repository;

import my.payment_pagsafe.domain.entity.Pagsafe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PagsafeRepository extends JpaRepository<Pagsafe, UUID> {
}

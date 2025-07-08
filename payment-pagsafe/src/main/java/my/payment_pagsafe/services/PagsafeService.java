package my.payment_pagsafe.services;

import my.payment_pagsafe.domain.dto.PaymentDto;
import my.payment_pagsafe.domain.entity.Pagsafe;
import my.payment_pagsafe.domain.entity.PagsafeStatus;
import my.payment_pagsafe.infrastructure.kafka.KafkaProducer;
import my.payment_pagsafe.infrastructure.repository.PagsafeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PagsafeService {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private PagsafeRepository pagsafeRepository;

    public Pagsafe payment(PaymentDto paymentDto) {
        if (paymentDto.getProcessId() == null) {
            throw new IllegalArgumentException("ProcessId não pode ser nulo");
        }

        double taxD = 0.05;
        BigDecimal taxBD = BigDecimal.valueOf(taxD);
        BigDecimal tax = paymentDto.getAmount()
                .multiply(taxBD)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = paymentDto.getAmount()
                .subtract(tax)
                .setScale(2, RoundingMode.HALF_UP);


        Pagsafe pagsafe = new Pagsafe();
        pagsafe.setId(paymentDto.getProcessId());
        pagsafe.setUsername(paymentDto.getUsername());
        pagsafe.setName("PagSafe");
        pagsafe.setAmount(paymentDto.getAmount());
        pagsafe.setTax(tax);
        pagsafe.setTotal(total);
        pagsafe.setStatus(PagsafeStatus.SUCCESS);

        PaymentDto dto = new PaymentDto();
        dto.setProcessId(pagsafe.getId());
        dto.setUsername(pagsafe.getUsername());
        dto.setName(pagsafe.getName());
        dto.setAmount(pagsafe.getAmount());
        dto.setTax(pagsafe.getTax());
        dto.setTotal(pagsafe.getTotal());
        dto.setStatus(PagsafeStatus.SUCCESS);

        kafkaProducer.paymentProducer(dto);
        return pagsafeRepository.save(pagsafe);
    }
}

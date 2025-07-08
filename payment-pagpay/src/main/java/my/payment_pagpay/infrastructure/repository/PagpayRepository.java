package my.payment_pagpay.infrastructure.repository;

import my.payment_pagpay.domain.entity.Pagpay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PagpayRepository extends JpaRepository<Pagpay, UUID> {
}

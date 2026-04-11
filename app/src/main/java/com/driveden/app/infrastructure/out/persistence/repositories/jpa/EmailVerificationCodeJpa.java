package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driveden.app.infrastructure.out.persistence.entity.EmailVerificationCodeEntity;

public interface EmailVerificationCodeJpa extends JpaRepository<EmailVerificationCodeEntity, Long> {

    EmailVerificationCodeEntity findTopByEmailAndCodeAndUsedFalseOrderByIdDesc(String email, String code);

}

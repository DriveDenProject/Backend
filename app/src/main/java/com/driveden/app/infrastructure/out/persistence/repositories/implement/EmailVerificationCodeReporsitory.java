package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import org.springframework.stereotype.Repository;

import com.driveden.app.domain.auth.model.EmailVerification;
import com.driveden.app.infrastructure.out.persistence.entity.EmailVerificationCodeEntity;
import com.driveden.app.infrastructure.out.persistence.mappers.EmailVerificationCodeMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.EmailVerificationCodeJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EmailVerificationCodeReporsitory {

    private final EmailVerificationCodeJpa emailVerificationCodeJpa;

    public EmailVerification findTopByEmailAndCodeAndUsedFalseOrderByIdDesc(String email, String code) {

        EmailVerificationCodeEntity entity = emailVerificationCodeJpa.findTopByEmailAndCodeAndUsedFalseOrderByIdDesc(email, code);
   
        return EmailVerificationCodeMapper.toDomain(entity);
    }

    public EmailVerification save(EmailVerification emailVerification) {
        return EmailVerificationCodeMapper.toDomain(
            emailVerificationCodeJpa.save(
                EmailVerificationCodeMapper.toEntity(emailVerification)
            )
        );
    }
}

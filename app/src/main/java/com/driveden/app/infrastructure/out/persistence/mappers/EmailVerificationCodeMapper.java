package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.auth.model.EmailVerification;
import com.driveden.app.infrastructure.out.persistence.entity.EmailVerificationCodeEntity;

public class EmailVerificationCodeMapper {

    public static EmailVerificationCodeEntity toEntity(EmailVerification emailVerification) {
        if (emailVerification == null) {
            return null;
        }
        EmailVerificationCodeEntity entity = new EmailVerificationCodeEntity();
        entity.setId(emailVerification.getId());
        entity.setEmail(emailVerification.getEmail());
        entity.setCode(emailVerification.getCode());
        entity.setExpiresAt(emailVerification.getExpiresAt());
        entity.setUsed(emailVerification.isUsed());
        return entity;
    }

    public static EmailVerification toDomain(EmailVerificationCodeEntity entity) {
        if (entity == null) {
            return null;
        }
        return EmailVerification.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .code(entity.getCode())
                .expiresAt(entity.getExpiresAt())
                .used(entity.isUsed())
                .build();
    }
}

package com.driveden.app.infrastructure.out.persistence.mappers;

import com.driveden.app.domain.paymentMethods.model.PaymentMethodDomain;
import com.driveden.app.infrastructure.out.persistence.entity.PaymentMethodsEntity;

public class PaymentMethodMapper {

    public static PaymentMethodDomain toDomain(PaymentMethodsEntity entity) {
        if (entity == null) {
            return null;
        }
        return new PaymentMethodDomain(
                entity.getId(),
                entity.getName(),
                entity.getCode(),
                entity.getDescription(),
                entity.getIsActive(),
                entity.getProvider()
        );
    }

    public static PaymentMethodsEntity toEntity(PaymentMethodDomain domain) {
        if (domain == null) {
            return null;
        }
        PaymentMethodsEntity entity = new PaymentMethodsEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setCode(domain.getCode());
        entity.setDescription(domain.getDescription());
        entity.setIsActive(domain.getIsActive());
        entity.setProvider(domain.getProvider());
        return entity;
    }

}

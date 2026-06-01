package com.driveden.app.domain.paymentMethods.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMethodDomain {

    private Long id;
    private String name;
    private String code;
    private String description;
    private Boolean isActive;
    private String provider;

}

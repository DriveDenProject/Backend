package com.driveden.app.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginDTO {

    @NotBlank(message = "idToken is required")
    private String idToken;
}

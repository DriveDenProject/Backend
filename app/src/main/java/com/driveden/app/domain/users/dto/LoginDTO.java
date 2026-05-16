package com.driveden.app.domain.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    private String email;
    @NotBlank(message = "password is required")
    private String password;

}

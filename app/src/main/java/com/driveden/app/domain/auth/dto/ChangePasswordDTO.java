package com.driveden.app.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDTO (

     @NotBlank(message = "newPassword is required")
     @Size(min = 8, max = 72, message = "newPassword must be between 8 and 72 characters")
     String newPassword

) {

}

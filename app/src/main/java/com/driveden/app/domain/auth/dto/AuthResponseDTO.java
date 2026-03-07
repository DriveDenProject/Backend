package com.driveden.app.domain.auth.dto;

public record AuthResponseDTO(
    String email,
    String token,
    String message
) {

}

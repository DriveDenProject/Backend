package com.driveden.app.domain.users.dto;

public record RegisterUserDTO(
    String username,
    String email,
    String password,
    String phoneNumber
) {

}

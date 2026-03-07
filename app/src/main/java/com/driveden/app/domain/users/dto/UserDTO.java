package com.driveden.app.domain.users.dto;

public record UserDTO(
    long id,
    String username,
    String email,
    String phoneNumber,
    String createdAt
) {

}

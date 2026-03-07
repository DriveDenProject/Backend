package com.driveden.app.domain.auth.dto;

public record AuthenticatedUser(
    Long id,
    String email,
    String createdAt
) {

}

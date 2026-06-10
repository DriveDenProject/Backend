package com.driveden.app.domain.users.dto;

public record UserDetailsDTO(
    Long vehicleId,
    String username,
    String nickname,
    String brand,
    String model,
    Integer year
) {

}

package com.driveden.app.application.tools;

import org.springframework.ai.tool.annotation.Tool;

import org.springframework.stereotype.Service;


import com.driveden.app.application.services.SecurityContextService;
import com.driveden.app.application.services.UsersService;
import com.driveden.app.domain.users.dto.UserDetailsDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class vehicleTools {

    private final UsersService usersService;
    private final SecurityContextService securityContextService;

    @Tool(description = "Obtiene los detalles del vehículo principal de un usuario dado su ID.")
    public UserDetailsDTO getPrimaryVehicleDetails() {

        return usersService.getPrimaryVehicleDetailsByUserId(
            securityContextService.getAuthenticatedUserId()
        );

    }

}

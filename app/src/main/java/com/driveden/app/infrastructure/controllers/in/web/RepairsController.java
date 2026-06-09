package com.driveden.app.infrastructure.controllers.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.services.RepairService;
import com.driveden.app.domain.auth.dto.AuthenticatedUser;
import com.driveden.app.domain.repairs.dto.RegisterRepairDTO;
import com.driveden.app.domain.repairs.dto.RepairResponseDTO;
import com.driveden.app.utils.CustomResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/repairs")
@Validated
public class RepairsController {

    private final RepairService repairService;

    @PostMapping
    public CustomResponse<RepairResponseDTO> registerRepair(
            @Valid @RequestBody RegisterRepairDTO registerRepairDTO,
            Authentication authentication
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                repairService.registerRepair(registerRepairDTO, authenticatedUser.id()),
                HttpStatus.CREATED,
                "Repair registered successfully"
        );
    }
}

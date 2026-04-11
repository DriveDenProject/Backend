package com.driveden.app.infrastructure.controllers.in.web;

import org.springframework.security.core.Authentication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.services.AuthService;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.auth.dto.AuthRefreshRequestDTO;
import com.driveden.app.domain.auth.dto.AuthResponseDTO;
import com.driveden.app.domain.users.dto.LoginDTO;
import com.driveden.app.utils.CustomResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public CustomResponse<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) throws CustomException {

        AuthResponseDTO authResponse = authService.login(loginDTO);

        return new CustomResponse<AuthResponseDTO>(authResponse, HttpStatus.OK, "Login successful");
    }

    @PostMapping("/refresh")
    public CustomResponse<AuthResponseDTO> refresh(@RequestBody AuthRefreshRequestDTO request) {
        return new CustomResponse<AuthResponseDTO>(authService.refresh(request), HttpStatus.OK, "Tokens actualizados");
    }

    @GetMapping("/me")
    public CustomResponse<?> me(Authentication authentication){
        return new CustomResponse<Object>(authentication.getPrincipal(), HttpStatus.ACCEPTED, "Hiii");
    }
}
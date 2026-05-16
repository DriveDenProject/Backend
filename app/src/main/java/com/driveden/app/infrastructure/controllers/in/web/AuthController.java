package com.driveden.app.infrastructure.controllers.in.web;

import org.springframework.security.core.Authentication;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.services.AuthService;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.auth.dto.AuthRefreshRequestDTO;
import com.driveden.app.domain.auth.dto.AuthResponseDTO;
import com.driveden.app.domain.auth.dto.ChangePasswordDTO;
import com.driveden.app.domain.users.dto.LoginDTO;
import com.driveden.app.utils.CustomResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public CustomResponse<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) throws CustomException {

        AuthResponseDTO authResponse = authService.login(loginDTO);

        return new CustomResponse<AuthResponseDTO>(authResponse, HttpStatus.OK, "Login successful");
    }

    @PostMapping("/refresh")
    public CustomResponse<AuthResponseDTO> refresh(@Valid @RequestBody AuthRefreshRequestDTO request) {
        return new CustomResponse<AuthResponseDTO>(authService.refresh(request), HttpStatus.OK, "Tokens actualizados");
    }

    @GetMapping("/me")
    public CustomResponse<?> me(Authentication authentication){
        return new CustomResponse<Object>(authentication.getPrincipal(), HttpStatus.ACCEPTED, "Hiii");
    }

    @PostMapping("/send-code")
    public CustomResponse<?> sendCode(@RequestParam @NotBlank @Email String email) {
        authService.createAndSendCode(email);
        return new CustomResponse<>("Codigo de verificación enviado", HttpStatus.OK, "Código de verificación enviado");
    }

    @PostMapping("/verify-code")
    public CustomResponse<?> verify(
            @RequestParam @NotBlank @Email String email,
            @RequestParam @NotBlank @Pattern(regexp = "\\d{6}", message = "code must be 6 digits") String code
    ) {
        boolean isValid = authService.verifyCode(email, code);
        return new CustomResponse<>(isValid, HttpStatus.OK, isValid ? "Código válido" : "Código inválido");
    }

    @PostMapping("/change-password")
    public CustomResponse<?> changePassword(
            @RequestParam @NotBlank @Email String email,
            @RequestParam @NotBlank @Pattern(regexp = "\\d{6}", message = "code must be 6 digits") String code,
            @Valid @RequestBody ChangePasswordDTO newPassword
    ) {
        boolean success = authService.verifyCodeAndSetPassword(email, code, newPassword);
        return new CustomResponse<>(success, HttpStatus.OK, "Contraseña actualizada correctamente");
    }
    
}

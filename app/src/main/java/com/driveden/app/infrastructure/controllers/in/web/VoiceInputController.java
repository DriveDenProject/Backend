package com.driveden.app.infrastructure.controllers.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.usecase.ProcessVoiceInputUseCase;
import com.driveden.app.domain.auth.dto.AuthenticatedUser;
import com.driveden.app.domain.voice.dto.ProcessVoiceInputRequestDTO;
import com.driveden.app.domain.voice.dto.VoiceClassificationResponseDTO;
import com.driveden.app.utils.CustomResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voice-input")
@Validated
public class VoiceInputController {

    private final ProcessVoiceInputUseCase processVoiceInputUseCase;

    @PostMapping("/process")
    public CustomResponse<VoiceClassificationResponseDTO> processVoiceInput(
            @Valid @RequestBody ProcessVoiceInputRequestDTO request,
            Authentication authentication,
            HttpServletRequest servletRequest
    ) {
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();

        return new CustomResponse<>(
                processVoiceInputUseCase.process(request.getText(), authenticatedUser.id(), clientIp(servletRequest)),
                HttpStatus.OK,
                "Voice input processed successfully"
        );
    }

    private String clientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}

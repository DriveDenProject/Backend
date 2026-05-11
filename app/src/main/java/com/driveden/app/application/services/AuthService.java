package com.driveden.app.application.services;


import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.auth.dto.AuthRefreshRequestDTO;
import com.driveden.app.domain.auth.dto.AuthResponseDTO;
import com.driveden.app.domain.auth.dto.ChangePasswordDTO;
import com.driveden.app.domain.auth.model.EmailVerification;
import com.driveden.app.domain.users.dto.LoginDTO;
import com.driveden.app.domain.users.model.Users;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.EmailVerificationCodeReporsitory;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UsersRepository;
import com.driveden.app.utils.TokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final EmailVerificationCodeReporsitory emailVerificationCodeRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailService emailService;

    public AuthResponseDTO login(LoginDTO loginDTO) throws CustomException {

        Users user = usersRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new CustomException("Invalid credentials", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        String accessToken = tokenService.generateToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return new AuthResponseDTO(user.getEmail(), accessToken, refreshToken, "Login successful");
    }
    
    public AuthResponseDTO refresh(AuthRefreshRequestDTO request){

        String refreshToken = request.getRefreshToken();

        DecodedJWT decoded = tokenService.verifyToken(refreshToken);

        String type = decoded.getClaim("type").asString();
        if(type == null || !type.equals("refresh")){
            throw new RuntimeException("Token inválido");
        }

        Long userId = Long.valueOf(decoded.getSubject());

        Users user = usersRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String newAccessToken = tokenService.generateToken(user);
        String newRefreshToken = tokenService.generateRefreshToken(user);

        return new AuthResponseDTO(user.getEmail(), newAccessToken, newRefreshToken, "Tokens actualizados");
    }

    public void createAndSendCode(String email) {

        String code = emailService.generateCode();

        EmailVerification entity = EmailVerification.builder()
            .email(email)
            .code(code)
            .expiresAt(LocalDateTime.now().plusMinutes(10))
            .used(false)
            .build();
    
        emailVerificationCodeRepo.save(entity);

        emailService.sendVerificationCode(email, code);
    }

    public boolean verifyCode(String email, String code) {

        EmailVerification record = emailVerificationCodeRepo
            .findTopByEmailAndCodeAndUsedFalseOrderByIdDesc(email, code);

        if (record == null) return false;

        if (record.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        record.setUsed(true);
        emailVerificationCodeRepo.save(record);

        return true;
    }

    public boolean verifyCodeAndSetPassword(String email, String code, ChangePasswordDTO newPassword) {

        EmailVerification record = emailVerificationCodeRepo
            .findTopByEmailAndCodeAndUsedFalseOrderByIdDesc(email, code);

        if (record == null) throw new CustomException("Código de verificación o Usuario inválido", HttpStatus.BAD_REQUEST);

        if (record.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CustomException("Código de verificación expirado", HttpStatus.BAD_REQUEST);
        }

        Users user = usersRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        user.setPassword(passwordEncoder.encode(newPassword.newPassword()));
        usersRepository.save(user);

        record.setUsed(true);
        emailVerificationCodeRepo.save(record);

        return true;
    }
}

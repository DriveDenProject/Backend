package com.driveden.app.application.services;


import java.time.LocalDateTime;
import java.util.HashSet;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.driveden.app.application.ports.out.GoogleAuthPort;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.auth.dto.AuthRefreshRequestDTO;
import com.driveden.app.domain.auth.dto.AuthResponseDTO;
import com.driveden.app.domain.auth.dto.ChangePasswordDTO;
import com.driveden.app.domain.auth.dto.GoogleLoginDTO;
import com.driveden.app.domain.auth.model.AuthProvider;
import com.driveden.app.domain.auth.model.EmailVerification;
import com.driveden.app.domain.auth.model.GoogleUserInfo;
import com.driveden.app.domain.users.dto.LoginDTO;
import com.driveden.app.domain.users.model.Users;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.EmailVerificationCodeReporsitory;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UsersRepository;
import com.driveden.app.utils.TokenService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final EmailVerificationCodeReporsitory emailVerificationCodeRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final GoogleAuthPort googleAuthPort;

    public AuthResponseDTO login(LoginDTO loginDTO) throws CustomException {

        Users user = usersRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new CustomException("Invalid credentials", HttpStatus.UNAUTHORIZED));

        if (user.getPassword() == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        return buildAuthResponse(user, "Login successful");
    }

    @Transactional
    public AuthResponseDTO googleLogin(GoogleLoginDTO googleLoginDTO) {
        GoogleUserInfo googleUserInfo = googleAuthPort.validateIdToken(googleLoginDTO.getIdToken());

        if (googleUserInfo.getEmail() == null || !Boolean.TRUE.equals(googleUserInfo.getEmailVerified())) {
            throw new CustomException("Google email is not verified", HttpStatus.UNAUTHORIZED);
        }

        Users user = usersRepository.findByGoogleId(googleUserInfo.getGoogleId())
                .or(() -> usersRepository.findByEmailIgnoreCase(googleUserInfo.getEmail()))
                .map(existingUser -> linkGoogleAccount(existingUser, googleUserInfo))
                .orElseGet(() -> createGoogleUser(googleUserInfo));

        Users savedUser = usersRepository.save(user);
        return buildAuthResponse(savedUser, "Login successful");
    }

    private AuthResponseDTO buildAuthResponse(Users user, String message) {
        String accessToken = tokenService.generateToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        return new AuthResponseDTO(user.getEmail(), accessToken, refreshToken, message);
    }

    private Users linkGoogleAccount(Users user, GoogleUserInfo googleUserInfo) {
        if (user.getAuthProviders() == null) {
            user.setAuthProviders(new HashSet<>());
        }

        user.getAuthProviders().add(AuthProvider.GOOGLE);
        user.setGoogleId(googleUserInfo.getGoogleId());
        user.setEmailVerified(Boolean.TRUE.equals(googleUserInfo.getEmailVerified()));

        if (googleUserInfo.getPicture() != null && !googleUserInfo.getPicture().isBlank()) {
            user.setProfilePicture(googleUserInfo.getPicture());
        }

        return user;
    }

    private Users createGoogleUser(GoogleUserInfo googleUserInfo) {
        return Users.builder()
                .username(resolveGoogleUsername(googleUserInfo))
                .email(googleUserInfo.getEmail())
                .password(null)
                .phoneNumber(null)
                .createdAt(LocalDateTime.now())
                .authProviders(new HashSet<>(java.util.Set.of(AuthProvider.GOOGLE)))
                .googleId(googleUserInfo.getGoogleId())
                .profilePicture(googleUserInfo.getPicture())
                .emailVerified(Boolean.TRUE.equals(googleUserInfo.getEmailVerified()))
                .build();
    }

    private String resolveGoogleUsername(GoogleUserInfo googleUserInfo) {
        String username = googleUserInfo.getName();
        if (username == null || username.isBlank()) {
            username = googleUserInfo.getGivenName();
        }
        if (username == null || username.isBlank()) {
            username = googleUserInfo.getEmail().split("@")[0];
        }

        return username.length() > 50 ? username.substring(0, 50) : username;
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

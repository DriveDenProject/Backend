package com.driveden.app.application.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.driveden.app.application.ports.out.GoogleAuthPort;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.auth.dto.AuthResponseDTO;
import com.driveden.app.domain.auth.dto.GoogleLoginDTO;
import com.driveden.app.domain.auth.model.AuthProvider;
import com.driveden.app.domain.auth.model.GoogleUserInfo;
import com.driveden.app.domain.users.model.Users;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.EmailVerificationCodeReporsitory;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UsersRepository;
import com.driveden.app.utils.TokenService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private EmailVerificationCodeReporsitory emailVerificationCodeRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private EmailService emailService;

    @Mock
    private GoogleAuthPort googleAuthPort;

    @InjectMocks
    private AuthService authService;

    @Test
    void googleLoginCreatesUserWhenEmailDoesNotExist() {
        GoogleLoginDTO request = googleLoginRequest();
        GoogleUserInfo googleUserInfo = verifiedGoogleUser();

        when(googleAuthPort.validateIdToken("valid-id-token")).thenReturn(googleUserInfo);
        when(usersRepository.findByGoogleId("google-sub-123")).thenReturn(Optional.empty());
        when(usersRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.empty());
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> {
            Users user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(tokenService.generateToken(any(Users.class))).thenReturn("access-token");
        when(tokenService.generateRefreshToken(any(Users.class))).thenReturn("refresh-token");

        AuthResponseDTO response = authService.googleLogin(request);

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        verify(usersRepository).save(userCaptor.capture());
        Users savedUser = userCaptor.getValue();

        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("Google User", savedUser.getUsername());
        assertEquals("google-sub-123", savedUser.getGoogleId());
        assertEquals("https://example.com/avatar.png", savedUser.getProfilePicture());
        assertTrue(savedUser.getAuthProviders().contains(AuthProvider.GOOGLE));
        assertEquals(null, savedUser.getPassword());
        assertEquals("test@example.com", response.email());
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
    }

    @Test
    void googleLoginLinksExistingLocalUserByEmail() {
        GoogleLoginDTO request = googleLoginRequest();
        GoogleUserInfo googleUserInfo = verifiedGoogleUser();
        Users existingUser = Users.builder()
                .id(10L)
                .username("Local User")
                .email("test@example.com")
                .password("encoded-password")
                .phoneNumber("123")
                .createdAt(LocalDateTime.now().minusDays(1))
                .authProviders(new HashSet<>(Set.of(AuthProvider.LOCAL)))
                .emailVerified(false)
                .build();

        when(googleAuthPort.validateIdToken("valid-id-token")).thenReturn(googleUserInfo);
        when(usersRepository.findByGoogleId("google-sub-123")).thenReturn(Optional.empty());
        when(usersRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(existingUser));
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tokenService.generateToken(any(Users.class))).thenReturn("access-token");
        when(tokenService.generateRefreshToken(any(Users.class))).thenReturn("refresh-token");

        AuthResponseDTO response = authService.googleLogin(request);

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        verify(usersRepository).save(userCaptor.capture());
        Users savedUser = userCaptor.getValue();

        assertEquals(10L, savedUser.getId());
        assertEquals("Local User", savedUser.getUsername());
        assertEquals("encoded-password", savedUser.getPassword());
        assertEquals("123", savedUser.getPhoneNumber());
        assertTrue(savedUser.getAuthProviders().contains(AuthProvider.LOCAL));
        assertTrue(savedUser.getAuthProviders().contains(AuthProvider.GOOGLE));
        assertEquals("google-sub-123", savedUser.getGoogleId());
        assertTrue(savedUser.getEmailVerified());
        assertEquals("test@example.com", response.email());
    }

    @Test
    void googleLoginRejectsUnverifiedGoogleEmail() {
        GoogleLoginDTO request = googleLoginRequest();
        GoogleUserInfo googleUserInfo = verifiedGoogleUser();
        googleUserInfo.setEmailVerified(false);

        when(googleAuthPort.validateIdToken("valid-id-token")).thenReturn(googleUserInfo);

        CustomException exception = assertThrows(CustomException.class, () -> authService.googleLogin(request));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("Google email is not verified", exception.getMessage());
    }

    private GoogleLoginDTO googleLoginRequest() {
        GoogleLoginDTO request = new GoogleLoginDTO();
        request.setIdToken("valid-id-token");
        return request;
    }

    private GoogleUserInfo verifiedGoogleUser() {
        return GoogleUserInfo.builder()
                .email("test@example.com")
                .emailVerified(true)
                .name("Google User")
                .givenName("Google")
                .familyName("User")
                .picture("https://example.com/avatar.png")
                .googleId("google-sub-123")
                .build();
    }
}

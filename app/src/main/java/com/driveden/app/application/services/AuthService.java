package com.driveden.app.application.services;


import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.auth.dto.AuthResponseDTO;
import com.driveden.app.domain.users.dto.LoginDTO;
import com.driveden.app.domain.users.model.Users;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UsersRepository;
import com.driveden.app.utils.TokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthResponseDTO login(LoginDTO loginDTO) throws CustomException {

        Users user = usersRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new CustomException("Invalid credentials", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        String token = tokenService.generateToken(user);

        return new AuthResponseDTO(user.getEmail(), token, "Login successful");
    }
}

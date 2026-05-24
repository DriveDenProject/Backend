package com.driveden.app.application.services;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.driveden.app.domain.users.dto.UserDTO;
import com.driveden.app.domain.users.dto.UserDetailsDTO;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.users.dto.RegisterUserDTO;
import com.driveden.app.domain.users.model.Users;
import com.driveden.app.infrastructure.out.persistence.mappers.UsersMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UserVehicleRepository;
import com.driveden.app.infrastructure.out.persistence.repositories.implement.UsersRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository UsersRepository;
    private final UserVehicleRepository userVehicleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO registerUser(RegisterUserDTO userDTO) {

        Users userToRegister = UsersMapper.registerDtoToDomain(userDTO);
        userToRegister.setPassword(passwordEncoder.encode(userToRegister.getPassword()));

        try {
    
        //Guardar Nuevo user em la DB
        Users savedUser = UsersRepository.save(userToRegister);
        
        //Retornar el nuevo user registrado convertido a DTO
        return UsersMapper.domaintoDTO(savedUser);
            
        } catch (DataIntegrityViolationException e) {
            //Si ocurre un error al guardar el nuevo usuario, lanzar una excepción personalizada
            throw new CustomException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        
    }

    @Transactional
    public UserDTO getUserByEmail(String email) {

        //Buscar por email en la DB
        Optional<Users> founduser = UsersRepository.findByEmail(email);
        //Si no se encuentra un usuario con ese email, lanzar una excepción
        if(founduser.isEmpty()) {
            throw new CustomException("Email not Found", HttpStatus.NOT_FOUND);
        }
        //Retornar el user encontrado convertido a DTO
        //founduser.get() devuelve el objeto Users dentro del Optional, y luego se convierte a DTO antes de retornarlo
        return UsersMapper.domaintoDTO(founduser.get());
    }

    public Users findUserById(Long id) {
        Optional<Users> foundUser = UsersRepository.findById(id);
        if (foundUser.isEmpty()) {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
        return foundUser.get();
    }

    public UserDetailsDTO getPrimaryVehicleDetailsByUserId(Long userId) {
        var detailsProjection = userVehicleRepository.findPrimaryVehicleByUserId(userId);

        if (detailsProjection == null) {
            throw new CustomException("Primary vehicle not found for user", HttpStatus.NOT_FOUND);
        }
        
        return new UserDetailsDTO(
            detailsProjection.getUsername(),
            detailsProjection.getNickname(),
            detailsProjection.getBrand(),
            detailsProjection.getModel(),
            detailsProjection.getYear()
        );
    }
}

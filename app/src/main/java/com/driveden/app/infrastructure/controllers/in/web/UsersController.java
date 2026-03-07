package com.driveden.app.infrastructure.controllers.in.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driveden.app.application.services.UsersService;
import com.driveden.app.domain.users.dto.RegisterUserDTO;
import com.driveden.app.domain.users.dto.UserDTO;
import com.driveden.app.utils.CustomResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/post")
    public CustomResponse<UserDTO> registerUser(@RequestBody RegisterUserDTO userDTO) {  

        return new CustomResponse<UserDTO>(
            //Enviar Body del Registro
            usersService.registerUser(userDTO), 
            //Status HTTP 201 Created
            HttpStatus.CREATED, 
            //Mensaje personalizado
            "User registered successfully"
        );
    }
    
    @GetMapping("/getByEmail")
    public CustomResponse<UserDTO> getUserByEmail(@RequestParam String email) {

        return new CustomResponse<UserDTO>(
            //Enviar Body de la Busqueda
            usersService.getUserByEmail(email), 
            //Status HTTP 200 OK
            HttpStatus.OK, 
            //Mensaje personalizado
            "User retrieved successfully"
        );
    }


}

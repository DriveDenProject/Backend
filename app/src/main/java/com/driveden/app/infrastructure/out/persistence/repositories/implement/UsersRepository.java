package com.driveden.app.infrastructure.out.persistence.repositories.implement;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.driveden.app.domain.users.model.Users;
import com.driveden.app.infrastructure.out.persistence.mappers.UsersMapper;
import com.driveden.app.infrastructure.out.persistence.repositories.jpa.UsersJpa;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UsersRepository {

    private final UsersJpa usersJpa;

    public Optional<Users> findByEmail(String email) {
        //Retornar el user encontrado convertido a un objeto de dominio
        return usersJpa.findByEmail(email)
            //Si no se encuentra un usuario con ese email, retornar null
            .map(UsersMapper::entitytoDomain);
    }

    public Optional<Users> findByEmailIgnoreCase(String email) {
        return usersJpa.findByEmailIgnoreCase(email)
            .map(UsersMapper::entitytoDomain);
    }

    public Optional<Users> findById(Long id) {
        //Retornar el user encontrado convertido a un objeto de dominio
        return usersJpa.findById(id)
            //Si no se encuentra un usuario con ese id, retornar null
            .map(UsersMapper::entitytoDomain);
    }

    public Optional<Users> findByGoogleId(String googleId) {
        return usersJpa.findByGoogleId(googleId)
            .map(UsersMapper::entitytoDomain);
    }

    public Users save(Users user) {
        //Retornar el user guardado convertido a un objeto de dominio
        return UsersMapper.entitytoDomain(
            //Guardar User en la DB
            usersJpa.save(
                //Convertir el objeto de dominio a una entidad antes de guardarlo
                UsersMapper.domaintoEntity(user)
            )
        );
    }

}

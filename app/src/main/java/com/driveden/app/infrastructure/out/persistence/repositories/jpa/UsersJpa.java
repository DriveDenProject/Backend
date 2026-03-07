package com.driveden.app.infrastructure.out.persistence.repositories.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.driveden.app.infrastructure.out.persistence.entity.UsersEntity;

public interface UsersJpa extends JpaRepository<UsersEntity, Long> {

    Optional<UsersEntity> findByEmail(String email);

}

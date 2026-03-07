package com.driveden.app.domain.users.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDateTime createdAt;
    
}



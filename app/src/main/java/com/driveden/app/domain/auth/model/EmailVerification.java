package com.driveden.app.domain.auth.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailVerification {

    private Long id;
    private String email;
    private String code;    
    private LocalDateTime expiresAt;
    private boolean used;
    
}

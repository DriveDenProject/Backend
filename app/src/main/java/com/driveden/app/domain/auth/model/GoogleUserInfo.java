package com.driveden.app.domain.auth.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleUserInfo {

    private String email;
    private Boolean emailVerified;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
    private String googleId;
}

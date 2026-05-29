package com.driveden.app.application.ports.out;

import com.driveden.app.domain.auth.model.GoogleUserInfo;

public interface GoogleAuthPort {

    GoogleUserInfo validateIdToken(String idToken);
}

package com.driveden.app.infrastructure.out.oauth;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.driveden.app.application.ports.out.GoogleAuthPort;
import com.driveden.app.common.exception.CustomException;
import com.driveden.app.domain.auth.model.GoogleUserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Component
public class GoogleOAuthAdapter implements GoogleAuthPort {

    private final GoogleIdTokenVerifier verifier;

    public GoogleOAuthAdapter(@Value("${google.oauth.client-id}") String googleClientId)
            throws GeneralSecurityException, IOException {
        this.verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance()
        )
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    @Override
    public GoogleUserInfo validateIdToken(String idToken) {
        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken == null) {
                throw new CustomException("Invalid Google idToken", HttpStatus.UNAUTHORIZED);
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            return GoogleUserInfo.builder()
                    .email(payload.getEmail())
                    .emailVerified(payload.getEmailVerified())
                    .name((String) payload.get("name"))
                    .givenName((String) payload.get("given_name"))
                    .familyName((String) payload.get("family_name"))
                    .picture((String) payload.get("picture"))
                    .googleId(payload.getSubject())
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new CustomException("Unable to validate Google idToken", HttpStatus.UNAUTHORIZED);
        }
    }
}

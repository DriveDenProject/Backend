package com.driveden.app.utils;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.driveden.app.domain.users.model.Users;

@Service
public class TokenService  {

    @Value("${api.security.token.service}")
    private String SECRET_KEY;

    public String generateToken(Users user){
        try {
            // Generacion de Algoritmo con Secret Key
            var algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                //Cual es el servidor que firma el token
                .withIssuer("DRIVEDEN API")
                //Informacion del Usuario que se desea Guardar en el Token JWT
                //El Subject es el Dato Principal del Token JWT, en este caso, el id del Usuario
                .withSubject(user.getId().toString())
                //PayLoad
                .withClaim("email", user.getEmail())
                .withClaim("created_At", user.getCreatedAt().toString())
                //Fecha de Caducacion
                .withExpiresAt(fechaExpiracion())
                //.withClaim(key, value) -> Uso de json con datos Necesarios
                //Algoritmo Usado
                .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error al generar el token: ", exception);
        }
    }

    public Instant fechaExpiracion(){
                //A partir de Ahora
                //Agregar 2 horas
        return Instant.now().plusSeconds(7200); 
    }

    //Obtener Responsable del Token "usuario_id"
    public String getSubject(String tokenJWT){
        try {
            var algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.require(algorithm)
                // specify any specific claim validations
                .withIssuer("DRIVEDEN API")
                // reusable verifier instance
                .build()
                //Verificar Token Obtenido
                .verify(tokenJWT)
                //Retornar Datos del Token
                .getSubject();     
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT invalido o expirado", exception);
        }
    }

    //Verificar y Obtener El Payload del Token
    public DecodedJWT verifyToken(String token){
    var algorithm = Algorithm.HMAC256(SECRET_KEY);

    return JWT.require(algorithm)
            .withIssuer("DRIVEDEN API")
            .build()
            .verify(token);
}
    
}

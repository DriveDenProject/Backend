package com.driveden.app.config.Security;

import java.io.IOException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.driveden.app.domain.auth.dto.AuthenticatedUser;
import com.driveden.app.utils.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        //Obtener el Token JWT del Header de Autorizacion
        String tokenJWT = getToken(request);
        //Validar el Token JWT
        if(tokenJWT != null){

            //Obtener Payload del Token
            DecodedJWT decodedJWT = tokenService.verifyToken(tokenJWT);

            //Construir DTO
            AuthenticatedUser user = new AuthenticatedUser(
                Long.valueOf(tokenService.getSubject(tokenJWT)),
                decodedJWT.getClaim("email").asString(), 
                decodedJWT.getClaim("created_At").asString()
            );

            //Crear un objeto de Autenticacion con el Subject del Token JWT
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            List.of()
                    );
            //Establecer el objeto de Autenticacion en el Contexto de Seguridad de Spring
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //Continuar con la Ejecucion del Filtro
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request){
        //Obtener el Header de Autorizacion
        var authHeader = request.getHeader("Authorization");
        //Validar que el Header no sea nulo y que Empiece con Bearer
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        //Remover la Palabra Bearer del Header, para obtener el Token JWT
        return authHeader.replace("Bearer ", "");
    }
}
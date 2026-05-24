package com.tiago.gestion_turnos.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secretKey;
    private static final long EXPIRATION_TIME = 86400000; // 24 horas en milisegundos
    
    /**
     * Genera un token JWT firmado con HMAC512.
     *
     * @param subject El subject (email del usuario) a incluir en el token.
     * @return El token completo.
     */
    public String generateToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        String token = JWT.create()
                .withSubject(subject)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC512(secretKey));

        return token;
    }

    /**
     * Verifica si un token JWT es valido.
     *
     * @param token El token recibido (puede incluir "Bearer " al inicio)
     * @return true si es válido, false en caso contrario.
     */
    public boolean verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secretKey)).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * Obtiene el subject (email) almacenado dentro del token JWT.
     * @param token El token JWT recibido (con o sin "Bearer")
     * @return El subject (email) o null si el token no es valido.
     */
    public String getSubject(String token) {
        try {
            DecodedJWT decoded = JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(token);

            return decoded.getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}


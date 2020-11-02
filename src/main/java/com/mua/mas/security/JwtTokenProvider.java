package com.mua.mas.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mua.mas.model.LoginCredential;
import com.mua.mas.model.UserPrincipal;
import com.mua.mas.property.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import java.util.Date;

public class JwtTokenProvider {
    private static final Logger logger= LoggerFactory.getLogger(JwtTokenProvider.class);

    public String generateTokenWithPrefix(Authentication authentication) {
        return Properties.tokenPrefix + generateToken(((UserPrincipal) authentication.getPrincipal()));
    }

    public String generateTokenWithPrinciple(UserPrincipal userPrincipal) {
        return generateToken(userPrincipal);
    }

    public String generateToken(String username){
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + Properties.expirationTime))
                .sign(Algorithm.HMAC256(Properties.secret.getBytes()));
    }

    public String generateToken(LoginCredential loginCredential){
        return generateToken(loginCredential.getUsername());
    }

    public String generateToken(UserPrincipal userPrincipal) {
        return generateToken(userPrincipal.getUsername());
    }

    public String getUserNameFromJWT(String token) {
        return JWT.require(Algorithm.HMAC256(Properties.secret.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            JWT.require(Algorithm.HMAC256(Properties.secret.getBytes())).build().verify(authToken);
            return true;
        } catch (JWTVerificationException ex) {
            logger.error(ex.getLocalizedMessage());
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
package com.example.swcamp_p03.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.swcamp_p03.config.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.swcamp_p03.config.jwt.JwtProperties.*;

@Component
public class JwtTokenUtils {

//    @Value("${jwt.secretKey}")
    private String JWT_SECRET = "aaaaa";

    public String generateJwtToken(UserDetailsImpl userDetailsImpl) {
        return JWT.create()
                .withSubject(SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim(CLAIM_USERID, userDetailsImpl.getUser().getUserId())
                .sign(Algorithm.HMAC512(JWT_SECRET));
    }


    public Long extractUserId(String jwtToken) {

        return JWT.require(Algorithm.HMAC512(JWT_SECRET)).build()
                .verify(jwtToken)
                .getClaim(CLAIM_USERID).asLong();
    }
}

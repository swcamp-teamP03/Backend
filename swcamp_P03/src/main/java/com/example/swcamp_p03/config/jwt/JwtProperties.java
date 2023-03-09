package com.example.swcamp_p03.config.jwt;

public interface JwtProperties {
    String CLAIM_USER_EMAIL = "USER_EMAIL";
    String SUBJECT = "JwtToken";
    int EXPIRATION_TIME = 864000000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}

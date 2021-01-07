package com.devlhse.minhasfinancas.security.constants;


public class SecurityConstants {

    public static final String JWT_SECRET = "05b1fa66-e62a-4c59-8bf2-37bc16390f20";
    public static final long EXPIRATION_TIME = 3_600_000; // 60 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL_WITH_ROOT = "/api/usuarios/";
    public static final String SIGN_UP_URL = "/api/usuarios";
}


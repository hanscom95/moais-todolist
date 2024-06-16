package com.moais.todo.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public interface JwtProperties {

	@Value("${jwt.secret}")
	String SECRET = "xfX9nyUC9sTvRuH";
	@Value("${jwt.expiration}")
	int EXPIRATION_TIME = 216000;
	@Value("${jwt.prefix}")
	String TOKEN_PREFIX = "Bearer ";
	@Value("${jwt.header}")
	String HEADER_STRING= "Authorization";

}

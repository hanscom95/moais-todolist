package com.moais.todo.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moais.todo.transfer.dto.request.UserLoginRequest;
import com.moais.todo.util.UserDetail;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException {

		log.info("JwtAuthenticationFilter request : call");
		ObjectMapper om = new ObjectMapper();
		UserLoginRequest userLoginRequest = null;
		try {
			userLoginRequest = om.readValue(request.getInputStream(), UserLoginRequest.class);
			log.info("JwtAuthenticationFilter userLoginRequest : {}", userLoginRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//해당 객체로 로그인 시도를 위한 유저네임패스워드 authenticationToken 생성
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(
				userLoginRequest.getUserId(),
				userLoginRequest.getPassword());


		Authentication authentication =
			authenticationManager.authenticate(authenticationToken);

		return authentication;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		UserDetail userDetail = (UserDetail) authResult.getPrincipal();
		String jwtToken = JWT.create()
			.withSubject(userDetail.getUsername())
			.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
			.withClaim("id", userDetail.getId())
			.withClaim("username", userDetail.getUsername())
			.sign(Algorithm.HMAC512(JwtProperties.SECRET));

		log.info("JwtAuthenticationFilter successfulAuthentication jwtToken: {}", JwtProperties.TOKEN_PREFIX+jwtToken);


		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
	}
}

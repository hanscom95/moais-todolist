package com.moais.todo.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import com.moais.todo.datasource.todolist.entity.UserEntity;
import com.moais.todo.datasource.todolist.repository.UserRepository;
import com.moais.todo.util.UserDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private UserRepository userRepository;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		try {
			String header = request.getHeader(JwtProperties.HEADER_STRING);
			if (header != null && header.startsWith(JwtProperties.TOKEN_PREFIX)) {
				String token = request.getHeader(JwtProperties.HEADER_STRING)
					.replace(JwtProperties.TOKEN_PREFIX, "");

				String id = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
					.getClaim("id").asString();

				if (id != null) {
					UserEntity user = userRepository.findByIdAndWithdrawAtIsNull(id).orElseThrow();

					UserDetail principalDetails = new UserDetail(user);
					Authentication authentication = new UsernamePasswordAuthenticationToken(
						principalDetails,
						null,
						principalDetails.getAuthorities());

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (Exception e) {
			request.setAttribute("trace", "TokenError");
		}

		doFilter(request, response, chain);
	}
}

package com.moais.todo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.moais.todo.datasource.todolist.repository.UserRepository;
import com.moais.todo.jwt.JwtAuthenticationEntryPoint;
import com.moais.todo.jwt.JwtAuthenticationFilter;
import com.moais.todo.jwt.JwtAuthorizationFilter;
import com.moais.todo.jwt.JwtExceptionFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepository userRepository;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.apply(new MyCustomDsl())
			.and()
			.authorizeRequests()
			// .antMatchers("/todo/**").authenticated()
			.antMatchers("/todo/**", "/user/withdrawal").hasAnyRole("MANAGER", "USER")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.disable();
			// .loginPage("/login")       // formLogin이 필요한 경우, /login 으로 보낸다.
			// .loginProcessingUrl("/loginProc")   // login 주소가 호출이 되면 시큐리티가 낚이채서 대신 로그인을 진행
			// .defaultSuccessUrl("/").permitAll();
	}

	// @Bean
	// public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
	// 	return http.getSharedObject(AuthenticationManagerBuilder.class)
	// 		.userDetailsService(userDetailService)
	// 		.passwordEncoder(new BCryptPasswordEncoder())
	// 		.and().build();
	// }
	public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
			http
				.addFilter(new JwtAuthenticationFilter(authenticationManager))
				.addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
		}
	}
}

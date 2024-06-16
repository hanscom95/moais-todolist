package com.moais.todo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moais.todo.service.UserApiService;
import com.moais.todo.transfer.dto.request.UserLoginRequestDto;
import com.moais.todo.transfer.dto.request.UserRequestDto;
import com.moais.todo.transfer.dto.response.DefaultErrorResponseDto;
import com.moais.todo.transfer.dto.response.DefaultResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Setter
@Tag(
	name = "User",
	description = "User api"
)
public class UserController {

	private final UserApiService userApiService;

	@Autowired
	UserController(UserApiService userApiService) {
		this.userApiService = userApiService;
	}

	@GetMapping(path = "/user/find/name")
	@Operation(summary = "getFindName - user name 조회")
	@ApiResponses( value = {
		@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "fail", content = @Content(schema = @Schema(implementation = DefaultErrorResponseDto.class)))
	})
	public ResponseEntity<Object> getFindName(
			@RequestParam(name = "name") String name
	){
		return userApiService.getFindName(name);
	}

	@PostMapping(path = "/user/signup")
	@Operation(summary = "signupUser - user 생성")
	@ApiResponses( value = {
		@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "fail", content = @Content(schema = @Schema(implementation = DefaultErrorResponseDto.class)))
	})
	public ResponseEntity<Object> signupUser(
		@RequestBody @Valid UserRequestDto userRequestDto
	){
		return userApiService.signupUser(userRequestDto);
	}

	@PostMapping(path = "/login")
	@Operation(summary = "loginUser - user login")
	@ApiResponses( value = {
		@ApiResponse(responseCode = "200", description = "success", content = {}, headers = {
			@Header(name = "Authorization", description = "Bearer Token", schema = @Schema(type = "string"), required = true)
		}),
		@ApiResponse(responseCode = "400", description = "fail", content = @Content(schema = @Schema(implementation = DefaultErrorResponseDto.class)))
	})
	public ResponseEntity<Object> loginUser(
		@RequestBody @Valid UserLoginRequestDto userLoginRequestDto
	){
		return userApiService.loginUser(userLoginRequestDto);
	}

	@PutMapping(path = "/user/withdrawal")
	@Operation(summary = "회원탈퇴 - withdrawal")
	@ApiResponses( value = {
		@ApiResponse(responseCode = "200", description = "success", content = {}, headers = {
			@Header(name = "Authorization", description = "Bearer Token", schema = @Schema(type = "string"), required = true)
		}),
		@ApiResponse(responseCode = "400", description = "fail", content = @Content(schema = @Schema(implementation = DefaultErrorResponseDto.class)))
	})
	public ResponseEntity<Object> withdrawal(
		@RequestBody @Valid UserLoginRequestDto userLoginRequestDto,
		Authentication authentication
	){
		log.info("updateUserInfo userRequestDto : {}", userLoginRequestDto);
		log.info("updateUserInfo authentication : {}", authentication.getPrincipal());
		return userApiService.withdrawal(userLoginRequestDto);
	}
}

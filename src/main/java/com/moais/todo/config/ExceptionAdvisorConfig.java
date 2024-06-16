package com.moais.todo.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.moais.todo.transfer.dto.response.DefaultErrorResponseDto;

@ControllerAdvice
@RestController
public class ExceptionAdvisorConfig {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<DefaultErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

		BindingResult bindingResult = e.getBindingResult();

		StringBuilder builder = new StringBuilder();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			builder.append("[");
			builder.append(fieldError.getField());
			builder.append("] ");
			builder.append(fieldError.getDefaultMessage());
		}

		return new ResponseEntity<>(DefaultErrorResponseDto.of(
			"err_request_value_validation_check",
			builder.toString()),
			HttpStatus.BAD_REQUEST
		);
	}
}

package com.moais.todo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.moais.todo.service.TodoApiService;
import com.moais.todo.transfer.dto.request.TodoInsertRequestDto;
import com.moais.todo.transfer.dto.request.TodoStatusUpdateRequestDto;
import com.moais.todo.transfer.dto.response.DefaultErrorResponseDto;
import com.moais.todo.transfer.dto.response.DefaultResponseDto;

import io.swagger.v3.oas.annotations.Operation;
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
	name = "TODO",
	description = "TODO api"
)
public class TodoController {

	private final TodoApiService todoApiService;

	@Autowired
	TodoController(TodoApiService todoApiService) {
		this.todoApiService = todoApiService;
	}

	@GetMapping(path = "/todo/list")
	@Operation(summary = "getTodoList - todo list 조회")
	@ApiResponses( value = {
		@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "fail", content = @Content(schema = @Schema(implementation = DefaultErrorResponseDto.class)))
	})
	public ResponseEntity<Object> getTodoList(
		Authentication authentication
	){
		return todoApiService.getTodoList(authentication);
	}

	@GetMapping(path = "/todo/{todoUid}")
	@Operation(summary = "getTodo - uid 검색 조회")
	@ApiResponses( value = {
		@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "fail", content = @Content(schema = @Schema(implementation = DefaultErrorResponseDto.class)))
	})
	public ResponseEntity<Object> getTodo(
		@PathVariable Long todoUid,
		Authentication authentication
	){
		return todoApiService.getTodo(authentication, todoUid);
	}

	@GetMapping(path = "/todo/last")
	@Operation(summary = "getTodoLast - todo 마지막 항목 조회")
	@ApiResponses( value = {
		@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "fail", content = @Content(schema = @Schema(implementation = DefaultErrorResponseDto.class)))
	})
	public ResponseEntity<Object> getTodoLast(
		Authentication authentication
	){
		return todoApiService.getTodoLast(authentication);
	}

	@PostMapping(path = "/todo/add")
	@Operation(summary = "insertTodo - todo 추가")
	@ApiResponses( value = {
		@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "fail", content = @Content(schema = @Schema(implementation = DefaultErrorResponseDto.class)))
	})
	public ResponseEntity<Object> insertTodo(
		@RequestBody @Valid TodoInsertRequestDto todoInsertRequestDto,
		Authentication authentication
	){
		return todoApiService.insertTodo(authentication, todoInsertRequestDto);
	}

	@PutMapping(path = "/todo/update")
	@Operation(summary = "updateStatus - todo 상태 변경")
	@ApiResponses( value = {
		@ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = DefaultResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "fail", content = @Content(schema = @Schema(implementation = DefaultErrorResponseDto.class)))
	})
	public ResponseEntity<Object> updateStatus(
		@RequestBody @Valid TodoStatusUpdateRequestDto todoStatusUpdateRequestDto,
		Authentication authentication
	){
		return todoApiService.updateStatus(authentication, todoStatusUpdateRequestDto);
	}

}

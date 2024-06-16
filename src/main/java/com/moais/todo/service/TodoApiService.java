package com.moais.todo.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.moais.todo.datasource.todolist.entity.TodoEntity;
import com.moais.todo.datasource.todolist.repository.TodoRepository;
import com.moais.todo.snowflake.SnowflakeFactory;
import com.moais.todo.transfer.dto.request.TodoInsertRequestDto;
import com.moais.todo.transfer.dto.request.TodoStatusUpdateRequestDto;
import com.moais.todo.transfer.dto.response.DefaultErrorResponseDto;
import com.moais.todo.transfer.dto.response.UserResponseDto;
import com.moais.todo.transfer.dto.valid.TodoStatus;
import com.moais.todo.util.UserDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoApiService {

	private final MessageSource messageSource;
	private final TodoRepository todoRepository;

	@Autowired
	public TodoApiService(TodoRepository todoRepository, MessageSource messageSource) {

		this.todoRepository = todoRepository;

		this.messageSource = messageSource;
	}

	private static final String CODE_SUCCESS="todo.response.code.success.successCode";
	private static final String MSG_SUCCESS="todo.response.message.success.successMessage";
	private static final String CODE_ERROR="todo.response.code.success.errorCode";
	private static final String MSG_ERROR="todo.response.message.success.errorMessage";
	private static final String CODE_NOT_NULL="todo.response.code.error.nonNullValue";
	private static final String MSG_NOT_NULL="todo.response.message.error.nonNullValue";
	private static final String CODE_AUTH_FAILED="todo.response.code.error.authFailed";
	private static final String MSG_AUTH_FAILED="todo.response.message.error.authFailed";

	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
	public ResponseEntity<Object> getTodo(Authentication authentication, Long todoUid) {
		try {
			if (Objects.isNull(todoUid)) {
				return new ResponseEntity<>(DefaultErrorResponseDto.of(
					messageSource.getMessage(CODE_NOT_NULL, null, LocaleContextHolder.getLocale()),
					messageSource.getMessage(MSG_NOT_NULL, null, LocaleContextHolder.getLocale())),
					HttpStatus.NOT_FOUND
				);
			}

			if (!authentication.isAuthenticated()) {
				return new ResponseEntity<>(DefaultErrorResponseDto.of(
					messageSource.getMessage(CODE_AUTH_FAILED, null, LocaleContextHolder.getLocale()),
					messageSource.getMessage(MSG_AUTH_FAILED, null, LocaleContextHolder.getLocale())),
					HttpStatus.NOT_FOUND
				);
			}
			UserDetail userDetail = (UserDetail) authentication.getPrincipal();
			Long userUid = userDetail.getUid();
			log.info("getTodo userDetail userId : {}", userDetail.getId());
			log.info("getTodo userDetail userUid : {}", userDetail.getUid());
			log.info("getTodo userDetail userName : {}", userDetail.getUsername());


			Optional<TodoEntity> todoEntity = todoRepository.findTodoEntityByUidAndUserUid(todoUid, userUid);
			log.info("getTodo todoEntity : {}", todoEntity);

			if (todoEntity.isEmpty()) {
				return new ResponseEntity<>(DefaultErrorResponseDto.of(
					messageSource.getMessage(CODE_NOT_NULL, null, LocaleContextHolder.getLocale()),
					messageSource.getMessage(MSG_NOT_NULL, null, LocaleContextHolder.getLocale())),
					HttpStatus.NOT_FOUND
				);
			}
			HashMap<String, Object> resValue = new HashMap<>();
			resValue.put("todo", todoEntity.get());
			log.info("getTodo resValue : {}", todoEntity);

			// when
			return new ResponseEntity<>(UserResponseDto.of(
				messageSource.getMessage(CODE_SUCCESS, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_SUCCESS, null, LocaleContextHolder.getLocale()), resValue),
				HttpStatus.OK
			);
		} catch (Exception e) {
			log.error("getTodo error exception :  {}", e.getMessage());
			return new ResponseEntity<>(DefaultErrorResponseDto.of(
				messageSource.getMessage(CODE_ERROR, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_ERROR, null, LocaleContextHolder.getLocale())),
				HttpStatus.BAD_REQUEST
			);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
	public ResponseEntity<Object> getTodoList(Authentication authentication) {
		try {
			if (!authentication.isAuthenticated()) {
				return new ResponseEntity<>(DefaultErrorResponseDto.of(
					messageSource.getMessage(CODE_AUTH_FAILED, null, LocaleContextHolder.getLocale()),
					messageSource.getMessage(MSG_AUTH_FAILED, null, LocaleContextHolder.getLocale())),
					HttpStatus.NOT_FOUND
				);
			}
			UserDetail userDetail = (UserDetail) authentication.getPrincipal();
			Long userUid = userDetail.getUid();
			log.info("getTodoList userDetail userId : {}", userDetail.getId());
			log.info("getTodoList userDetail userUid : {}", userDetail.getUid());
			log.info("getTodoList userDetail userName : {}", userDetail.getUsername());


			List<TodoEntity> todoList = todoRepository.findTodoEntitiesByUserUidOrderByCreateAtDesc(userUid);

			log.info("getTodoList todoList : {}", todoList);


			if (todoList.isEmpty()) {
				return new ResponseEntity<>(DefaultErrorResponseDto.of(
					messageSource.getMessage(CODE_NOT_NULL, null, LocaleContextHolder.getLocale()),
					messageSource.getMessage(MSG_NOT_NULL, null, LocaleContextHolder.getLocale())),
					HttpStatus.NOT_FOUND
				);
			}

			HashMap<String, Object> resValue = new HashMap<>();
			resValue.put("todoList", todoList);
			log.info("getTodoList resValue : {}", resValue);

			// when
			return new ResponseEntity<>(UserResponseDto.of(
				messageSource.getMessage(CODE_SUCCESS, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_SUCCESS, null, LocaleContextHolder.getLocale()), resValue),
				HttpStatus.OK
			);
		} catch (Exception e) {
			log.error("getTodoList error exception :  {}", e.getMessage());
			return new ResponseEntity<>(DefaultErrorResponseDto.of(
				messageSource.getMessage(CODE_ERROR, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_ERROR, null, LocaleContextHolder.getLocale())),
				HttpStatus.BAD_REQUEST
			);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
	public ResponseEntity<Object> getTodoLast(Authentication authentication) {
		try {
			if (!authentication.isAuthenticated()) {
				return new ResponseEntity<>(DefaultErrorResponseDto.of(
					messageSource.getMessage(CODE_AUTH_FAILED, null, LocaleContextHolder.getLocale()),
					messageSource.getMessage(MSG_AUTH_FAILED, null, LocaleContextHolder.getLocale())),
					HttpStatus.NOT_FOUND
				);
			}
			UserDetail userDetail = (UserDetail) authentication.getPrincipal();
			Long userUid = userDetail.getUid();
			log.info("getTodoLast userDetail userId : {}", userDetail.getId());
			log.info("getTodoLast userDetail userUid : {}", userDetail.getUid());
			log.info("getTodoLast userDetail userName : {}", userDetail.getUsername());


			Optional<TodoEntity> todo = todoRepository.findTop1TodoEntityByUserUidOrderByCreateAtDesc(userUid);

			log.info("getTodoLast todo : {}", todo);


			if (todo.isEmpty()) {
				return new ResponseEntity<>(DefaultErrorResponseDto.of(
					messageSource.getMessage(CODE_NOT_NULL, null, LocaleContextHolder.getLocale()),
					messageSource.getMessage(MSG_NOT_NULL, null, LocaleContextHolder.getLocale())),
					HttpStatus.NOT_FOUND
				);
			}

			HashMap<String, Object> resValue = new HashMap<>();
			resValue.put("todo", todo);
			log.info("getTodoLast resValue : {}", resValue);

			// when
			return new ResponseEntity<>(UserResponseDto.of(
				messageSource.getMessage(CODE_SUCCESS, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_SUCCESS, null, LocaleContextHolder.getLocale()), resValue),
				HttpStatus.OK
			);
		} catch (Exception e) {
			log.error("getTodoLast error exception :  {}", e.getMessage());
			return new ResponseEntity<>(DefaultErrorResponseDto.of(
				messageSource.getMessage(CODE_ERROR, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_ERROR, null, LocaleContextHolder.getLocale())),
				HttpStatus.BAD_REQUEST
			);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
	public ResponseEntity<Object> insertTodo(Authentication authentication, TodoInsertRequestDto todoInsertRequestDto) {
		try {
			if (!authentication.isAuthenticated()) {
				return new ResponseEntity<>(DefaultErrorResponseDto.of(
					messageSource.getMessage(CODE_AUTH_FAILED, null, LocaleContextHolder.getLocale()),
					messageSource.getMessage(MSG_AUTH_FAILED, null, LocaleContextHolder.getLocale())),
					HttpStatus.UNAUTHORIZED
				);
			}
			UserDetail userDetail = (UserDetail) authentication.getPrincipal();
			Long userUid = userDetail.getUid();

			TodoEntity todoEntity = new TodoEntity().builder()
				.uid(SnowflakeFactory.create())
				.userUid(userUid)
				.title(todoInsertRequestDto.getTitle())
				.description(todoInsertRequestDto.getDescription())
				.status(todoInsertRequestDto.getStatus())
				.createAt(Timestamp.valueOf(LocalDateTime.now()))
				.build();

			log.info("insertTodo todoEntity : {}", todoEntity);

			TodoEntity todo = todoRepository.save(todoEntity);

			HashMap<String, Object> resValue = new HashMap<>();
			resValue.put("uid",  todo.getUid());
			resValue.put("name",  todo.getTitle());

			return new ResponseEntity<>(UserResponseDto.of(
				messageSource.getMessage(CODE_SUCCESS, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_SUCCESS, null, LocaleContextHolder.getLocale()), resValue),
				HttpStatus.OK
			);
		} catch (Exception e) {
			log.error("insertTodo error exception :  {}", e.getMessage());
			return new ResponseEntity<>(DefaultErrorResponseDto.of(
				messageSource.getMessage(CODE_ERROR, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_ERROR, null, LocaleContextHolder.getLocale())),
				HttpStatus.BAD_REQUEST
			);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
	public ResponseEntity<Object> updateStatus(Authentication authentication, TodoStatusUpdateRequestDto todoStatusUpdateRequestDto) {
		try {
			if (!authentication.isAuthenticated()) {
				return new ResponseEntity<>(DefaultErrorResponseDto.of(
					messageSource.getMessage(CODE_AUTH_FAILED, null, LocaleContextHolder.getLocale()),
					messageSource.getMessage(MSG_AUTH_FAILED, null, LocaleContextHolder.getLocale())),
					HttpStatus.UNAUTHORIZED
				);
			}

			Optional<TodoEntity> todoEntityOptional = todoRepository.findById(todoStatusUpdateRequestDto.getUId());
			if (todoEntityOptional.isEmpty()) {
				return new ResponseEntity<>(DefaultErrorResponseDto.of(
					messageSource.getMessage(CODE_NOT_NULL, null, LocaleContextHolder.getLocale()),
					messageSource.getMessage(MSG_NOT_NULL, null, LocaleContextHolder.getLocale())),
					HttpStatus.NOT_FOUND
				);
			}
			TodoEntity todoEntity = todoEntityOptional.get();
			TodoStatus beforeStatus = todoEntity.getStatus();

			TodoStatus status = todoStatusUpdateRequestDto.getStatus();

			if (status == TodoStatus.PENDING && !beforeStatus.equals(TodoStatus.IN_PROGRESS)) {
				log.error("updateStatus error !(status == \"PENDING\" && beforeStatus == \"IN_PROGRESS\")");
				return new ResponseEntity<>(DefaultErrorResponseDto.of(
					messageSource.getMessage(CODE_ERROR, null, LocaleContextHolder.getLocale()),
					messageSource.getMessage(MSG_ERROR, null, LocaleContextHolder.getLocale())),
					HttpStatus.BAD_REQUEST
				);
			}


			todoEntity.setStatus(status);
			todoEntity.setUpdateAt(Timestamp.valueOf(LocalDateTime.now()));

			log.info("updateStatus todoEntity : {}", todoEntity);

			TodoEntity todo = todoRepository.save(todoEntity);

			HashMap<String, Object> resValue = new HashMap<>();
			resValue.put("uid",  todo.getUid());
			resValue.put("status",  todo.getStatus());

			return new ResponseEntity<>(UserResponseDto.of(
				messageSource.getMessage(CODE_SUCCESS, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_SUCCESS, null, LocaleContextHolder.getLocale()), resValue),
				HttpStatus.OK
			);
		} catch (Exception e) {
			log.error("updateStatus error exception :  {}", e.getMessage());
			return new ResponseEntity<>(DefaultErrorResponseDto.of(
				messageSource.getMessage(CODE_ERROR, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_ERROR, null, LocaleContextHolder.getLocale())),
				HttpStatus.BAD_REQUEST
			);
		}
	}
}

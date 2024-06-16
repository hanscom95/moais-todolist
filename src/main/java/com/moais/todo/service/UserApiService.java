package com.moais.todo.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.moais.todo.datasource.todolist.entity.UserEntity;
import com.moais.todo.datasource.todolist.repository.UserRepository;
import com.moais.todo.snowflake.SnowflakeFactory;
import com.moais.todo.transfer.dto.request.UserLoginRequestDto;
import com.moais.todo.transfer.dto.request.UserRequestDto;
import com.moais.todo.transfer.dto.response.DefaultErrorResponseDto;
import com.moais.todo.transfer.dto.response.UserResponseDto;
import com.moais.todo.util.UserDetail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserApiService implements UserDetailsService {

	private final MessageSource messageSource;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserApiService(UserRepository userRepository, MessageSource messageSource, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;

		this.messageSource = messageSource;

		this.passwordEncoder = passwordEncoder;
	}

	private static final String CODE_SUCCESS="todo.response.code.success.successCode";
	private static final String MSG_SUCCESS="todo.response.message.success.successMessage";
	private static final String CODE_ERROR="todo.response.code.success.errorCode";
	private static final String MSG_ERROR="todo.response.message.success.errorMessage";
	private static final String CODE_NOT_NULL="todo.response.code.error.nonNullValue";
	private static final String MSG_NOT_NULL="todo.response.message.error.nonNullValue";
	private static final String CODE_ID_DUPLICATE="todo.response.code.error.idDuplicate";
	private static final String MSG_ID_DUPLICATE="todo.response.message.error.idDuplicate";
	private static final String CODE_NOT_MATCHED="todo.response.code.error.notMatched";
	private static final String MSG_NOT_MATCHED="todo.response.message.error.notMatched";


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("UserDetailService username : {}", username);
		UserEntity userEntity =  userRepository.findByIdAndWithdrawAtIsNull(username).orElseThrow();
		log.info("UserDetailService userEntity : {}", userEntity);

		if (userEntity != null) {
			return new UserDetail(userEntity);
		}
		return null;
	}

	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
	public ResponseEntity<Object> getFindName(String name) {
		try {
			// when
			HashMap<String, Object> resValue = new HashMap<>();

			return new ResponseEntity<>(UserResponseDto.of(
				messageSource.getMessage(CODE_SUCCESS, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_SUCCESS, null, LocaleContextHolder.getLocale()), resValue),
				HttpStatus.OK
			);
		} catch (Exception e) {
			log.error("getFindName error exception :  {}", e.getMessage());
			return new ResponseEntity<>(DefaultErrorResponseDto.of(
				messageSource.getMessage(CODE_NOT_NULL, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_NOT_NULL, null, LocaleContextHolder.getLocale())),
				HttpStatus.BAD_REQUEST
			);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
	public ResponseEntity<Object> signupUser(UserRequestDto userRequestDto) {
		try {
			if (!userRepository.findUserId(userRequestDto.getId()).isEmpty()) {
				return new ResponseEntity<>(DefaultErrorResponseDto.of(
					messageSource.getMessage(CODE_ID_DUPLICATE, null, LocaleContextHolder.getLocale()),
					messageSource.getMessage(MSG_ID_DUPLICATE, null, LocaleContextHolder.getLocale())),
					HttpStatus.NOT_FOUND
				);
			}

			String name = userRequestDto.getName();
			String password = passwordEncoder.encode(userRequestDto.getPassword());


			UserEntity userEntity = new UserEntity().builder()
				.uid(SnowflakeFactory.create())
				.id(userRequestDto.getId())
				.password(password)
				.name(name)
				.createAt(Timestamp.valueOf(LocalDateTime.now()))
				.role("ROLE_MANAGER")
				.build();

			log.info("signupUser userEntity : {}", userEntity);

			UserEntity user = userRepository.save(userEntity);

			HashMap<String, Object> resValue = new HashMap<>();
			resValue.put("uid",  user.getUid());
			resValue.put("name",  user.getName());

			return new ResponseEntity<>(UserResponseDto.of(
				messageSource.getMessage(CODE_SUCCESS, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_SUCCESS, null, LocaleContextHolder.getLocale()), resValue),
				HttpStatus.OK
			);
		} catch (Exception e) {
			log.error("signupUser error exception :  {}", e.getMessage());
			return new ResponseEntity<>(DefaultErrorResponseDto.of(
				messageSource.getMessage(CODE_ERROR, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_ERROR, null, LocaleContextHolder.getLocale())),
				HttpStatus.BAD_REQUEST
			);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
	public ResponseEntity<Object> loginUser(UserLoginRequestDto userLoginRequestDto) {
		try {
			HashMap<String, Object> resValue = new HashMap<>();

			return new ResponseEntity<>(UserResponseDto.of(
				messageSource.getMessage(CODE_SUCCESS, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_SUCCESS, null, LocaleContextHolder.getLocale()), resValue),
				HttpStatus.OK
			);
		} catch (Exception e) {
			log.error("loginUser error exception :  {}", e.getMessage());
			return new ResponseEntity<>(DefaultErrorResponseDto.of(
				messageSource.getMessage(CODE_ERROR, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_ERROR, null, LocaleContextHolder.getLocale())),
				HttpStatus.BAD_REQUEST
			);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE)
	public ResponseEntity<Object> withdrawal(UserLoginRequestDto userLoginRequestDto) {
		try {
			Optional<UserEntity> userEntityOptional = userRepository.findUserEntityById(userLoginRequestDto.getUserId());
			log.info("withdrawal userEntityOptional : {}", userEntityOptional);
			log.info("withdrawal userEntityOptional get : {}", userEntityOptional.get());
			if (userEntityOptional.isEmpty()) {
				return new ResponseEntity<>(DefaultErrorResponseDto.of(
					messageSource.getMessage(CODE_NOT_MATCHED, null, LocaleContextHolder.getLocale()),
					messageSource.getMessage(MSG_NOT_MATCHED, null, LocaleContextHolder.getLocale())),
					HttpStatus.NOT_FOUND
				);
			}

			UserEntity userEntity = userEntityOptional.get();
			userEntity.setWithdrawAt(Timestamp.valueOf(LocalDateTime.now()));

			userRepository.save(userEntity);
			log.info("withdrawal user id : {}", userEntity.getUid());
			log.info("withdrawal user withdrawAt : {}", userEntity.getWithdrawAt());

			HashMap<String, Object> resValue = new HashMap<>();
			resValue.put("uid",  userEntity.getUid());
			resValue.put("withdrawAt",  userEntity.getWithdrawAt());

			return new ResponseEntity<>(UserResponseDto.of(
				messageSource.getMessage(CODE_SUCCESS, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_SUCCESS, null, LocaleContextHolder.getLocale()), resValue),
				HttpStatus.OK
			);
		} catch (Exception e) {
			log.error("withdrawal error exception :  {}", e.getMessage());
			return new ResponseEntity<>(DefaultErrorResponseDto.of(
				messageSource.getMessage(CODE_ERROR, null, LocaleContextHolder.getLocale()),
				messageSource.getMessage(MSG_ERROR, null, LocaleContextHolder.getLocale())),
				HttpStatus.BAD_REQUEST
			);
		}
	}
}

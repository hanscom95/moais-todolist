/******************************************************************************
 *
 *  (C) 2024 thmoon. All rights reserved.
 *  Any part of this source code can not be copied with any method without
 *  prior written permission from the author or authorized person.
 *
 ******************************************************************************/

package com.moais.todo.transfer.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@Schema(description = "User login 필드 값 요청")
public class UserLoginRequestDto {
    @NotEmpty
    @NotBlank
    @Schema(description = "User id", example = "id")
    String userId;

    @Schema(description = "password", example = "특수문자+숫자+대소문자, 8글자이상")
    @Size(min = 8, max = 20)
    String password;
}

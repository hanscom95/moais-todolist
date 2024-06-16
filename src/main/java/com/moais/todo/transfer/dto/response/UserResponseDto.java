/******************************************************************************
 *
 *  (C) 2024 tmoon. All rights reserved.
 *  Any part of this source code can not be copied with any method without
 *  prior written permission from the author or authorized person.
 *
 ******************************************************************************/

package com.moais.todo.transfer.dto.response;

import java.util.HashMap;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UserResponseDto {

    @Schema(description = "응답 코드", example = "OK")
    String status;
    @Schema(description = "응답 메시지", example = "successfully")
    String message;
    @Schema(description = "응답 내용")
    HashMap<String, Object> value;

    protected UserResponseDto(String status, String message, HashMap<String, Object> value) {
        this.status = status;
        this.message = message;
        this.value = value;
    }

    public UserResponseDto() {

    }

    public static UserResponseDto of(String status, String message, HashMap<String, Object> value) {
        return new UserResponseDto(status, message, value);
    }

}

/******************************************************************************
 *
 *  (C) 2024 thmoon. All rights reserved.
 *  Any part of this source code can not be copied with any method without
 *  prior written permission from the author or authorized person.
 *
 ******************************************************************************/

package com.moais.todo.transfer.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class DefaultErrorResponseDto {
    @Schema(description = "응답 코드", example = "fail")
    String errorCode;
    @Schema(description = "응답 메시지", example = "undefined case fail")
    String errorMessage;

    protected DefaultErrorResponseDto(String code, String message) {
        this.errorCode = code;
        this.errorMessage = message;
    }

    public DefaultErrorResponseDto() {

    }

    public static DefaultErrorResponseDto of(String code, String message) {
        return new DefaultErrorResponseDto(code, message);
    }
}

/******************************************************************************
 *
 *  (C) 2024 thmoon. All rights reserved.
 *  Any part of this source code can not be copied with any method without
 *  prior written permission from the author or authorized person.
 *
 ******************************************************************************/

package com.moais.todo.transfer.dto.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class DefaultResponseDto {
    private static final String CODE_SUCCESS="cert.response.code.success.successCode";

    private static final String MSG_SUCCESS="cert.response.message.success.successMessage";

    @Schema(description = "응답 코드", example = "success")
    String code;
    @Schema(description = "응답 메시지", example = "success")
    String message;
    @Schema(description = "응답 내용")
    List<Object> data;

    protected DefaultResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
        data = new ArrayList<>();
    }

    public DefaultResponseDto() {

    }

    public static DefaultResponseDto of(String code, String message) {
        return new DefaultResponseDto(code, message);
    }

    public static DefaultResponseDto of(MessageSource messageSource) {
        return new DefaultResponseDto(
                messageSource.getMessage(CODE_SUCCESS, null, LocaleContextHolder.getLocale()),
                messageSource.getMessage(MSG_SUCCESS, null, LocaleContextHolder.getLocale())
        );
    }

    public void addData (Object data) {
        this.data.add(data);
    }
}

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

import com.moais.todo.transfer.dto.valid.TodoStatus;
import com.moais.todo.util.ValidEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@Schema(description = "Todo Status 수정 요청")
public class TodoStatusUpdateRequestDto {
    @Schema(description = "todo id", example = "00000000000")
    Long uId;

    @Schema(description = "status", example = "'TODO', 'IN_PROGRESS', 'DONE', 'PENDING'")
    @ValidEnum(enumClass = TodoStatus.class)
    TodoStatus status;

}

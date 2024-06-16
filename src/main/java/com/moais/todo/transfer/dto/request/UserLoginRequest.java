/******************************************************************************
 *
 *  (C) 2024 thmoon. All rights reserved.
 *  Any part of this source code can not be copied with any method without
 *  prior written permission from the author or authorized person.
 *
 ******************************************************************************/

package com.moais.todo.transfer.dto.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String userId;
    private String password;
}

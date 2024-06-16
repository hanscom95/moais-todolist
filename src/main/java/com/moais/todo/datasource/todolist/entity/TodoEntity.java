/******************************************************************************
 *
 *  (C) 2024 thmoon. All rights reserved.
 *  Any part of this source code can not be copied with any method without
 *  prior written permission from the author or authorized person.
 *
 ******************************************************************************/

package com.moais.todo.datasource.todolist.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.moais.todo.transfer.dto.valid.TodoStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tbTodos")
@Table(name = "tb_todos")
@SuperBuilder
@Data
public class TodoEntity {

	@Id
	@Column(name = "uid", nullable = false)
	private Long uid;

	@Column(name = "user_uid")
	private Long userUid;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private TodoStatus status;

	@Column(name = "create_at")
	private Timestamp createAt;

	@Column(name = "update_at")
	private Timestamp updateAt;

}

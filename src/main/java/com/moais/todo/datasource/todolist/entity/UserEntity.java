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
import javax.persistence.Id;
import javax.persistence.Table;

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
@Entity(name = "tbUser")
@Table(name = "tb_user")
@SuperBuilder
@Data
public class UserEntity {

	@Id
	@Column(name = "uid", nullable = false)
	private Long uid;

	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "password")
	private String password;

	@Column(name = "role")
	private String role;

	@Column(name = "create_at")
	private Timestamp createAt;

	@Column(name = "withdraw_at")
	private Timestamp withdrawAt;

	@Column(name = "dormant_at")
	private Timestamp dormantAt;

}

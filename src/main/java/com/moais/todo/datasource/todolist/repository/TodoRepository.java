/******************************************************************************
 *
 *  (C) 2024 thmoon. All rights reserved.
 *  Any part of this source code can not be copied with any method without
 *  prior written permission from the author or authorized person.
 *
 ******************************************************************************/

package com.moais.todo.datasource.todolist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.moais.todo.datasource.todolist.entity.TodoEntity;

public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

	List<TodoEntity> findTodoEntitiesByUserUidOrderByCreateAtDesc(@Param("userUid") Long userUid);
	Optional<TodoEntity> findTodoEntityByUidAndUserUid(@Param("uId") Long uId, @Param("userUid") Long userUid);

	Optional<TodoEntity> findTop1TodoEntityByUserUidOrderByCreateAtDesc(@Param("userUid") Long userUid);

}

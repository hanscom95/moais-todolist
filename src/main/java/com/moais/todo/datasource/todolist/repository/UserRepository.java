/******************************************************************************
 *
 *  (C) 2024 thmoon. All rights reserved.
 *  Any part of this source code can not be copied with any method without
 *  prior written permission from the author or authorized person.
 *
 ******************************************************************************/

package com.moais.todo.datasource.todolist.repository;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moais.todo.datasource.todolist.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	@Query("SELECT a.uid as uid, a.id as id, a.name as name, a.role as role, a.createAt as createAt FROM tbUser AS a WHERE a.id = :id")
	Map<String, Object> findUserId(@Param("id") String id);
	@Query("SELECT a.uid as uid, a.id as id, a.name as name, a.role as role, a.createAt as createAt FROM tbUser AS a WHERE a.name = :name and a.password = :password")
	UserEntity login(@Param("name") String name, @Param("password") String password);
	@Query("SELECT a.uid as uid, a.id as id, a.password as password, a.name as name, a.role as role, a.createAt as createAt FROM tbUser AS a WHERE a.name = :name")
	Optional<UserEntity> findUserName(@Param("name") String name);

	Optional<UserEntity> findById(String id);

	Optional<UserEntity> findByIdAndWithdrawAtIsNull(String id);

	Optional<UserEntity> findByIdAndPassword(String id, String password);
	Optional<UserEntity> findUserEntityByIdAndPassword(String id, String password);
	Optional<UserEntity> findUserEntityById(String id);
}

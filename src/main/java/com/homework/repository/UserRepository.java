package com.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.homework.pojo.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	// 根据username取出一条记录
	@Query(value = "select * from user where username=:username", nativeQuery = true)
	User selectByUsername(String username);
}

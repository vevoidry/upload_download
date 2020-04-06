package com.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.homework.pojo.Title;

public interface TitleRepository extends JpaRepository<Title, Integer> {
	// 获取最新标题
	@Query(value = "select * from title order by id desc limit 1", nativeQuery = true)
	Title selectByLast();
}

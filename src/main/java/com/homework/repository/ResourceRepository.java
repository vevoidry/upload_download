package com.homework.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.homework.pojo.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Integer> {

	// 按升序取出相应的记录
	@Query(value = "select * from resource where directory_id=:directory_id and is_file=:is_file order by sort_number", nativeQuery = true)
	List<Resource> selectByDirectory_idIs_file(Integer directory_id, Boolean is_file);

	// 根据参数取出一条记录
	@Query(value = "select * from resource where directory_id=:directory_id and name=:name and is_file=:is_file", nativeQuery = true)
	Resource selectByDirectory_idNameIs_file(Integer directory_id, String name, Boolean is_file);

	// 根据参数取出降序的第一条记录
	@Query(value = "select * from resource where directory_id=:directory_id  and is_file=:is_file order by sort_number desc limit 1", nativeQuery = true)
	Resource selectByDirectory_idIs_fileSort_number(Integer directory_id, Boolean is_file);

	// 根据参数取出一条记录
	@Query(value = "select * from resource where directory_id=:directory_id  and is_file=:is_file and sort_number=:sort_number", nativeQuery = true)
	Resource selectByDirectory_idIs_fileSort_number(Integer directory_id, Boolean is_file, Integer sort_number);

	// 根据参数取出相关记录
	@Query(value = "select * from resource where directory_id=:directory_id and is_file=:is_file and sort_number>=:sort_number", nativeQuery = true)
	List<Resource> selectByDirectory_idIs_fileMoreThanSort_number(Integer directory_id, Boolean is_file,
			Integer sort_number);
}

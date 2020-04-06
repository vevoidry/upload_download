package com.homework.service;

import java.util.List;

import com.homework.pojo.Resource;

public interface ResourceService {

	Resource insert(Resource resource);
	
	Resource update(Resource resource);

	Resource selectById(Integer id);
	
	void deleteById(Integer id);

	List<Resource> selectByDirectory_idIs_file(Integer directory_id, Boolean is_file);

	Resource selectByDirectory_idNameIs_file(Integer directory_id, String name, Boolean is_file);
	
	Resource selectByDirectory_idIs_fileSort_number(Integer directory_id, Boolean is_file);

	Resource selectByDirectory_idIs_fileSort_number(Integer directory_id, Boolean is_file, Integer sort_number);
	
	List<Resource> selectByDirectory_idIs_fileMoreThanSort_number(Integer directory_id, Boolean is_file,
			Integer sort_number);
}

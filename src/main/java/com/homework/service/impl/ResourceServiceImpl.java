package com.homework.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.pojo.Resource;
import com.homework.repository.ResourceRepository;
import com.homework.service.ResourceService;

@Service
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceRepository resourceRepository;

	@Override
	public Resource insert(Resource resource) {
		return resourceRepository.save(resource);
	}

	@Override
	public Resource update(Resource resource) {
		return resourceRepository.save(resource);
	}

	@Override
	public Resource selectById(Integer id) {
		return resourceRepository.findById(id).get();
	}

	@Override
	public void deleteById(Integer id) {
		resourceRepository.deleteById(id);
	}

	@Override
	public List<Resource> selectByDirectory_idIs_file(Integer directory_id, Boolean is_file) {
		return resourceRepository.selectByDirectory_idIs_file(directory_id, is_file);
	}

	@Override
	public Resource selectByDirectory_idNameIs_file(Integer directory_id, String name, Boolean is_file) {
		return resourceRepository.selectByDirectory_idNameIs_file(directory_id, name, is_file);
	}

	@Override
	public Resource selectByDirectory_idIs_fileSort_number(Integer directory_id, Boolean is_file) {
		return resourceRepository.selectByDirectory_idIs_fileSort_number(directory_id, is_file);
	}

	@Override
	public Resource selectByDirectory_idIs_fileSort_number(Integer directory_id, Boolean is_file, Integer sort_number) {
		return resourceRepository.selectByDirectory_idIs_fileSort_number(directory_id, is_file, sort_number);
	}

	@Override
	public List<Resource> selectByDirectory_idIs_fileMoreThanSort_number(Integer directory_id, Boolean is_file,
			Integer sort_number) {
		return resourceRepository.selectByDirectory_idIs_fileMoreThanSort_number(directory_id, is_file, sort_number);
	}

}

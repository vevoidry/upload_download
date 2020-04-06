package com.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.pojo.User;
import com.homework.repository.UserRepository;
import com.homework.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;

	@Override
	public User selectByUsername(String username) {
		return userRepository.selectByUsername(username);
	}

	
}

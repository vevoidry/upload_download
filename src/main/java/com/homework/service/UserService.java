package com.homework.service;

import com.homework.pojo.User;

public interface UserService {
	User selectByUsername(String username);	
}

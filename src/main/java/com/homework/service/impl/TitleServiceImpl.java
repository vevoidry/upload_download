package com.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homework.pojo.Title;
import com.homework.repository.TitleRepository;
import com.homework.service.TitleService;

@Service
public class TitleServiceImpl implements TitleService {

	@Autowired
	private TitleRepository titleRepository;

	@Override
	public Title insert(Title title) {
		return titleRepository.save(title);
	}

	@Override
	public Title selectByLast() {
		return titleRepository.selectByLast();
	}

}

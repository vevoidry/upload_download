package com.homework.service;

import com.homework.pojo.Title;

public interface TitleService {

	Title insert(Title title);

	Title selectByLast();
}

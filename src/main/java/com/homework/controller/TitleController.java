package com.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.homework.pojo.Title;
import com.homework.service.impl.TitleServiceImpl;

@Controller
@RequestMapping("titles")
public class TitleController {

	@Autowired
	private TitleServiceImpl titleServiceImpl;

	// 跳转到设置标题页面
	@GetMapping
	public String admin_title(Model model) {
		Title title = titleServiceImpl.selectByLast();
		model.addAttribute("title", title);
		return "admin_title";
	}

	@PostMapping
	public String post(Title title) {
		titleServiceImpl.insert(title);
		return "redirect:/";
	}
}

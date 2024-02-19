package com.example.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.User;
import com.example.service.UserService;

@Controller
@RequestMapping("/login")
public class LoginController {
	@Autowired
	UserService userService;
	@GetMapping
	public String login(Model model) {
		System.out.println("login get");
		model.addAttribute("user", new User());
		return "login";
	}
	
	@PostMapping
	public String doLogin(@ModelAttribute("user") User user, Model model) {
		System.out.println("login post");
		return "home";
	}
}

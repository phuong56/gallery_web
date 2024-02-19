package com.example.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.entity.User;
import com.example.model.UserModel;
import com.example.service.UserService;

@Controller
public class RegisterController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserModel());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserModel user, Model model) {
    	System.out.println("register post");
        // Kiểm tra xem mật khẩu và mật khẩu xác nhận có khớp nhau không
        if (!user.getPassword().equals(user.getRepeatPassword())) {
            model.addAttribute("error", "Password and Confirm Password do not match");
            return "register";
        }

        // Kiểm tra xem người dùng đã tồn tại chưa
        if (userService.isUserAlreadyExist(user.getUsername(), user.getEmail())) {
            model.addAttribute("error", "User with this username or email already exists");
            return "register";
        }

        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        User user9 = new User(user.getUsername(), user.getPassword(), user.getEmail());

        // Lưu người dùng vào cơ sở dữ liệu
        userService.saveUser(user9);

        // Chuyển hướng đến trang đăng nhập sau khi đăng ký thành công
        model.addAttribute("success", true);
        return "register";
    }
}

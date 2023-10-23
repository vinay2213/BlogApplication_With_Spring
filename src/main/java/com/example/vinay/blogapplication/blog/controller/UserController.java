package com.example.vinay.blogapplication.blog.controller;

import com.example.vinay.blogapplication.blog.model.User;
import com.example.vinay.blogapplication.blog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        return "register";
    }

    @PostMapping("/register-user")
    public String registerUser(@ModelAttribute("user") User user,
                               @RequestParam("confirm_password") String password, Model model) {
        if (userService.findByName(user.getName()) != null || userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "User already exists...");

            return "register";
        }

        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Password Mismatch...");

            return "register";
        }

        userService.registerUser(user);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginUser() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        return "forgot-password";
    }

    @PostMapping("/forgot")
    public String checkPassword(String username, String password, Model model) {
        User user = userService.findByEmail(username);

        if (user == null) {
            model.addAttribute("error", "User not found..");
            return "forgot-password";
        }

        user.setPassword(password);
        userService.registerUser(user);
        return "redirect:/login";
    }
}

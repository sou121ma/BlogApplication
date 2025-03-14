package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final PostService postService;
    private final UserService userService;
    
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        User currentUser = userService.getCurrentUser(session);
        model.addAttribute("posts", postService.getAllPosts());
        model.addAttribute("currentUser", currentUser);
        return "index";
    }
}
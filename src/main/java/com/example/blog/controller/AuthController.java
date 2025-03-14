package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
//@RequiredArgsConstructor
@AllArgsConstructor
public class AuthController {
    
    private  UserService userService;
    
    @GetMapping("/login")
    public String loginForm(HttpSession session) {
        // Redirect to home if already logged in
        if (userService.getCurrentUser(session) != null) {
            return "redirect:/";
        }
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        try {
            User user = userService.login(username, password);
            userService.setUserInSession(session, user);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }
    
    @GetMapping("/register")
    public String registerForm(HttpSession session) {
        // Redirect to home if already logged in
        if (userService.getCurrentUser(session) != null) {
            return "redirect:/";
        }
        return "register";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam String username, 
                          @RequestParam String password,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        try {
            User user = userService.register(username, password);
            userService.setUserInSession(session, user);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        userService.logout(session);
        return "redirect:/";
    }
}
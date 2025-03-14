package com.example.blog.service;

import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    
    
    private final UserRepository userRepository;
    private static final String USER_SESSION_ATTR = "currentUser";
    
    public User register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // In a real app, you would hash the password
        return userRepository.save(user);
    }
    
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        
        // In a real app, you would verify the hashed password
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        
        return user;
    }
    
    public void setUserInSession(HttpSession session, User user) {
        session.setAttribute(USER_SESSION_ATTR, user.getId());
    }
    
    public User getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_SESSION_ATTR);
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId).orElse(null);
    }
    
    public void logout(HttpSession session) {
        session.removeAttribute(USER_SESSION_ATTR);
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
package com.example.blog.controller;

import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.service.CommentService;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;
    
    @PostMapping("/posts/{postId}/comments")
    public String addComment(@PathVariable Long postId,
                            @RequestParam String content,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser(session);
        
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to add a comment");
            return "redirect:/login";
        }
        
        try {
            Post post = postService.getPostById(postId);
            commentService.createComment(content, post, currentUser);
            redirectAttributes.addFlashAttribute("success", "Comment added successfully");
            return "redirect:/posts/" + postId;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/posts/" + postId;
        }
    }
    
    @PostMapping("/comments/{id}/delete")
    public String deleteComment(@PathVariable Long id,
                               @RequestParam Long postId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser(session);
        
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to delete a comment");
            return "redirect:/login";
        }
        
        try {
            commentService.deleteComment(id, currentUser);
            redirectAttributes.addFlashAttribute("success", "Comment deleted successfully");
            return "redirect:/posts/" + postId;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/posts/" + postId;
        }
    }
}
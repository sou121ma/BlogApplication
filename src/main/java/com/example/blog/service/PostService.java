package com.example.blog.service;

import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    
    @Autowired
    private final PostRepository postRepository;
    
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }
    
    public Post createPost(String title, String content, User author) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        return postRepository.save(post);
    }
    
    public Post updatePost(Long id, String title, String content, User currentUser) {
        Post post = getPostById(id);
        
        // Check if the current user is the author
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not authorized to edit this post");
        }
        
        post.setTitle(title);
        post.setContent(content);
        return postRepository.save(post);
    }
    
    public void deletePost(Long id, User currentUser) {
        Post post = getPostById(id);
        
        // Check if the current user is the author
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not authorized to delete this post");
        }
        
        postRepository.delete(post);
    }
    
    public List<Post> getPostsByUser(User user) {
        return postRepository.findByAuthorOrderByCreatedAtDesc(user);
    }
}
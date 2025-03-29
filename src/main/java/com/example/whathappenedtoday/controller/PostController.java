package com.example.whathappenedtoday.controller;

import com.example.whathappenedtoday.model.Post;
import com.example.whathappenedtoday.service.PostService;
import jakarta.servlet.http.HttpServletRequest; // Import for HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/posts") // Base path for post related endpoints
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        String content = requestBody.get("content");
        String ipAddress = request.getRemoteAddr(); // Get IP address from the request

        Post createdPost = postService.createPost(content, ipAddress);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED); // 201 Created status
    }
}
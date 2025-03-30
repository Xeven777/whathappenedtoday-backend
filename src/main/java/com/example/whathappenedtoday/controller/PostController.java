package com.example.whathappenedtoday.controller;

import com.example.whathappenedtoday.model.Post;
import com.example.whathappenedtoday.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for your React frontend (adjust port if needed)
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        String content = requestBody.get("content");
        String ipAddress = getClientIpAddress(request); // Use helper method to get IP

        Post createdPost = postService.createPost(content, ipAddress);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping // GET /api/posts - Get posts for today
    public ResponseEntity<List<Post>> getPostsForToday() {
        List<Post> todayPosts = postService.getPostsForToday();
        return new ResponseEntity<>(todayPosts, HttpStatus.OK);
    }

    // Helper method to get client IP address, handling proxies
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            // X-Forwarded-For can contain multiple IPs, get the first one (client IP)
            return xForwardedForHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr(); // Fallback to remoteAddr if X-Forwarded-For is not present
    }
}
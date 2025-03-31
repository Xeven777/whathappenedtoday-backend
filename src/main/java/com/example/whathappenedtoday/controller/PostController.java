package com.example.whathappenedtoday.controller;

import com.example.whathappenedtoday.model.Post;
import com.example.whathappenedtoday.service.PostService;
import com.example.whathappenedtoday.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = { "http://localhost:3000", "https://whathappndtoday.vercel.app" })
public class PostController {

    private static final Logger logger = Logger.getLogger(PostController.class.getName());
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        String content = requestBody.get("content");

        // Validate content
        if (content == null || content.trim().isEmpty()) {
            throw new BadRequestException("Post content cannot be empty");
        }

        try {
            String ipAddress = getClientIpAddress(request);
            Post createdPost = postService.createPost(content, ipAddress);
            logger.info("Created post with ID: " + createdPost.getId());
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.severe("Error creating post: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Post>> getPostsForDate(
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateParam) {
        try {
            LocalDate dateToFetch = dateParam != null ? dateParam : LocalDate.now();

            // Validate date isn't in the future
            if (dateToFetch.isAfter(LocalDate.now())) {
                throw new BadRequestException("Cannot retrieve posts for future dates");
            }

            List<Post> posts = postService.getPostsForDate(dateToFetch);
            logger.info("Retrieved " + posts.size() + " posts for date: " + dateToFetch);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Error retrieving posts: " + e.getMessage());
            throw e;
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        try {
            String xForwardedForHeader = request.getHeader("X-Forwarded-For");
            if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
                return xForwardedForHeader.split(",")[0].trim();
            }
            return request.getRemoteAddr();
        } catch (Exception e) {
            logger.warning("Error getting client IP: " + e.getMessage());
            return "unknown";
        }
    }
}
package com.example.whathappenedtoday.service;

import com.example.whathappenedtoday.model.Post;
import com.example.whathappenedtoday.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(String content, String ipAddress) {
        // 1. Validation
        if (content == null || content.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content cannot be empty.");
        }
        if (content.length() > 120) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content must be at most 120 characters.");
        }

        // 2. Check Daily Limit
        if (hasUserPostedToday(ipAddress)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You can only post once per day."); // 409 Conflict
        }

        // 3. Create and Save Post
        Post post = new Post(content, ipAddress);
        return postRepository.save(post);
    }

    public boolean hasUserPostedToday(String ipAddress) {
        LocalDateTime startOfDay = LocalDateTime.now(ZoneId.of("UTC")).toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.toLocalDate().atTime(LocalTime.MAX).atZone(ZoneId.of("UTC"))
                .toLocalDateTime(); // End of today in UTC

        List<Post> todayPosts = postRepository.findByIpAddressAndTimestampBetween(ipAddress, startOfDay, endOfDay);
        return !todayPosts.isEmpty(); // If there are posts from today, user has already posted
    }
}
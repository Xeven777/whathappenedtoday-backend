package com.example.whathappenedtoday.repository;

import com.example.whathappenedtoday.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

@Repository // This annotation indicates that this interface is a Spring Data repository
public interface PostRepository extends MongoRepository<Post, String> {
    // Custom query methods can be defined here if needed
    List<Post> findByIpAddressAndTimestampBetween(String ipAddress, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
// This interface extends MongoRepository, which provides CRUD operations for
// the Post entity.
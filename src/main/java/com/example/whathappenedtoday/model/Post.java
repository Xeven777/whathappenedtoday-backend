package com.example.whathappenedtoday.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "posts") // Specifies the MongoDB collection name
public class Post {

    @Id
    private String id; // MongoDB automatically generates IDs, but it's good to have it in the model
    private String content;
    private String ipAddress;
    private LocalDateTime timestamp;

    public Post() {
    }

    public Post(String content, String ipAddress) {
        this.content = content;
        this.ipAddress = ipAddress;
        this.timestamp = LocalDateTime.now(); // Automatically set timestamp when creating
    }

    // Getters and Setters (Generate these using your IDE or write them manually)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
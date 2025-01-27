package com.example.schedule.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Table(name = "authors")
@NoArgsConstructor
public class Author {

    @Id
    @Column(value = "author_id")
    private Long id;
    private String author;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;

    @Column(value = "is_active")
    private boolean isActive;

    @Builder
    public Author(Long id, String author, String email, LocalDateTime createdAt, LocalDateTime lastUpdated) {
        this.id = id;
        this.author = author;
        this.email = email;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
    }
}

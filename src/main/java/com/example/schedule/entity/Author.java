package com.example.schedule.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;

    @Column(value = "is_active")
    private boolean isActive;

    @Builder
    public Author(Long id, String name, String email, LocalDateTime createdAt, LocalDateTime lastUpdated, boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.isActive = isActive;
    }
}

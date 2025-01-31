package com.example.schedule.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Table(name = "schedules")
@NoArgsConstructor
public class Schedule {

    @Id
    @Column(value = "schedule_id")
    private Long id;

    @Column(value = "author_id")
    private Long authorId;
    private String todo;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;

    @Column(value = "is_active")
    private boolean isActive;

    @Builder
    public Schedule(Long id, Long authorId, String todo, String password, LocalDateTime createdAt, LocalDateTime lastUpdated, boolean isActive) {
        this.id = id;
        this.authorId = authorId;
        this.todo = todo;
        this.password = password;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.isActive = isActive;
    }
}

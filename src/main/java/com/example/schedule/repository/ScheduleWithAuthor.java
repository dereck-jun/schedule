package com.example.schedule.repository;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScheduleWithAuthor {

    private Long scheduleId;
    private String todo;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
    private String name;

    @Builder
    public ScheduleWithAuthor(Long scheduleId, String todo, LocalDateTime createdAt, LocalDateTime lastUpdated, String name) {
        this.scheduleId = scheduleId;
        this.todo = todo;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.name = name;
    }
}

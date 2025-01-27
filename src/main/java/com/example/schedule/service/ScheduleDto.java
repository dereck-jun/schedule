package com.example.schedule.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ScheduleDto {
    private final Long scheduleId;
    private final String author;
    private final String todo;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastUpdated;
}

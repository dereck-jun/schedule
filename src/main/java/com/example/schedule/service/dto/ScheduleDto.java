package com.example.schedule.service.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {
    private Long scheduleId;
    private String todo;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
    private Long authorId;
}

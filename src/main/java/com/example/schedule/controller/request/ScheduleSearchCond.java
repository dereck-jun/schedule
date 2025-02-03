package com.example.schedule.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class ScheduleSearchCond {
    private final String author;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate selectedDate;
}

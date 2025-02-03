package com.example.schedule.controller.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class GetSchedulesRequest {
    private final Long authorId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate selectedDate;

    @Positive
    private final Integer page;

    @Positive
    private final Integer size;

    public GetSchedulesRequest(Long authorId, LocalDate selectedDate, Integer page, Integer size) {
        this.authorId = authorId;
        this.selectedDate = selectedDate;
        this.page = page == null ? 1 : page;
        this.size = size == null ? 5 : size;
    }
}

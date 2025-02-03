package com.example.schedule.service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
    private List<T> contents;
    private int page;
    private int size;
    private long totalCount;
    private int totalPages;
}

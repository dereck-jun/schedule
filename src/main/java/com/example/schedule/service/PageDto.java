package com.example.schedule.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PageDto<T> {
    private final List<T> contents;
    private final int page;
    private final int size;
    private final long totalCount;
    private final int totalPages;
}

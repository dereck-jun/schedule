package com.example.schedule.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateScheduleRequest {

    private final String todo;
    private final String author;
    private final String password;
}

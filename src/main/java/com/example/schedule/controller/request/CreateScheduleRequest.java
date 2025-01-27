package com.example.schedule.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateScheduleRequest {
    private final String author;
    private final String email;
    private final String todo;
    private final String password;
}

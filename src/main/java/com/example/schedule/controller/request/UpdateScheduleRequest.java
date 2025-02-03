package com.example.schedule.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateScheduleRequest {

    @NotBlank
    @Size(max = 200)
    private final String todo;

    @NotBlank
    @Size(max = 45)
    private final String name;

    @NotBlank
    private final String password;
}

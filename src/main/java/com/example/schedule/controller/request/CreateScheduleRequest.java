package com.example.schedule.controller.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@RequiredArgsConstructor
public class CreateScheduleRequest {
    @NotBlank
    @Size(min = 2, max = 45)
    private final String name;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9-_]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    @Size(max = 45)
    private final String email;

    @NotBlank
    @Size(max = 200)
    private final String todo;

    @NotBlank
    private final String password;
}

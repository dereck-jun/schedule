package com.example.schedule.controller.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeleteScheduleRequest {
    @NotBlank
    private final String password;

    @JsonCreator
    public DeleteScheduleRequest(String password) {
        this.password = password;
    }
}

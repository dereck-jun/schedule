package com.example.schedule.controller.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class DeleteScheduleRequest {
    private final String password;

    @JsonCreator
    public DeleteScheduleRequest(String password) {
        this.password = password;
    }
}

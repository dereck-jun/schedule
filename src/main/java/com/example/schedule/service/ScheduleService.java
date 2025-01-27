package com.example.schedule.service;

import com.example.schedule.controller.request.CreateScheduleRequest;
import com.example.schedule.controller.request.DeleteScheduleRequest;
import com.example.schedule.controller.request.UpdateScheduleRequest;
import com.example.schedule.controller.request.ScheduleSearchCond;

import java.util.List;

public interface ScheduleService {

    ScheduleDto save(CreateScheduleRequest request);

    List<ScheduleDto> findSchedulesByAuthorOrSelectedDate(ScheduleSearchCond cond);

    ScheduleDto findById(Long scheduleId);

    ScheduleDto update(Long scheduleId, UpdateScheduleRequest dto);

    void delete(Long scheduleId, DeleteScheduleRequest request);
}

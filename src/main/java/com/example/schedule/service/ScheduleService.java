package com.example.schedule.service;

import com.example.schedule.controller.request.CreateScheduleRequest;
import com.example.schedule.controller.request.DeleteScheduleRequest;
import com.example.schedule.controller.request.GetSchedulesRequest;
import com.example.schedule.controller.request.UpdateScheduleRequest;
import com.example.schedule.repository.ScheduleWithAuthor;
import com.example.schedule.service.dto.PageDto;
import com.example.schedule.service.dto.ScheduleDto;

public interface ScheduleService {

    ScheduleDto save(CreateScheduleRequest request);

    PageDto<ScheduleWithAuthor> findAll(GetSchedulesRequest request);

    ScheduleDto findById(Long scheduleId);

    ScheduleDto update(Long scheduleId, UpdateScheduleRequest dto);

    void delete(Long scheduleId, DeleteScheduleRequest request);
}

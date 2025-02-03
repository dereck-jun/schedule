package com.example.schedule.controller;

import com.example.schedule.controller.request.CreateScheduleRequest;
import com.example.schedule.controller.request.DeleteScheduleRequest;
import com.example.schedule.controller.request.GetSchedulesRequest;
import com.example.schedule.controller.request.UpdateScheduleRequest;
import com.example.schedule.controller.response.ApiResponse;
import com.example.schedule.repository.ScheduleWithAuthor;
import com.example.schedule.service.dto.PageDto;
import com.example.schedule.service.dto.ScheduleDto;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ScheduleController {

    ApiResponse<ScheduleDto> createSchedule(@RequestBody CreateScheduleRequest request);

    ApiResponse<PageDto<ScheduleWithAuthor>> findAllSchedules(@ModelAttribute GetSchedulesRequest request);

    ApiResponse<ScheduleDto> findScheduleById(@PathVariable("scheduleId") Long id);

    ApiResponse<ScheduleDto> updateSchedule(@PathVariable("scheduleId") Long id, @RequestBody UpdateScheduleRequest request);

    ApiResponse<Void> deleteSchedule(@PathVariable("scheduleId") Long id, @RequestBody DeleteScheduleRequest request);
}

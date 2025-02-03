package com.example.schedule.controller;

import com.example.schedule.controller.request.CreateScheduleRequest;
import com.example.schedule.controller.request.DeleteScheduleRequest;
import com.example.schedule.controller.request.GetSchedulesRequest;
import com.example.schedule.controller.request.UpdateScheduleRequest;
import com.example.schedule.controller.response.ApiResponse;
import com.example.schedule.repository.ScheduleWithAuthor;
import com.example.schedule.service.dto.PageDto;
import com.example.schedule.service.dto.ScheduleDto;
import com.example.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleControllerImpl implements ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @Override
    public ApiResponse<ScheduleDto> createSchedule(@Valid @RequestBody CreateScheduleRequest request) {
        ScheduleDto createdSchedule = scheduleService.save(request);
        return ApiResponse.success(OK, createdSchedule, "일정 생성 성공");
    }

    @GetMapping
    @Override
    public ApiResponse<PageDto<ScheduleWithAuthor>> findAllSchedules(@Valid @ModelAttribute GetSchedulesRequest request) {
        PageDto<ScheduleWithAuthor> authorDtoPageDto = scheduleService.findAll(request);
        return ApiResponse.success(OK, authorDtoPageDto, "일정 전체 조회 성공");
    }

    @GetMapping("/{scheduleId}")
    @Override
    public ApiResponse<ScheduleDto> findScheduleById(@PathVariable(value = "scheduleId") Long id) {
        ScheduleDto findSchedule = scheduleService.findById(id);
        return ApiResponse.success(OK, findSchedule, "일정 단건 조회 성공");
    }

    @PatchMapping("/{scheduleId}")
    @Override
    public ApiResponse<ScheduleDto> updateSchedule(@PathVariable("scheduleId") Long id,
                                                   @Valid @RequestBody UpdateScheduleRequest request) {
        ScheduleDto updatedSchedule = scheduleService.update(id, request);
        return ApiResponse.success(OK, updatedSchedule, "일정 수정 성공");
    }

    @DeleteMapping("/{scheduleId}")
    @Override
    public ApiResponse<Void> deleteSchedule(@PathVariable("scheduleId") Long id,
                                            @Valid @RequestBody DeleteScheduleRequest request) {
        scheduleService.delete(id, request);
        return ApiResponse.success(OK, null, "일정 삭제 성공");
    }
}
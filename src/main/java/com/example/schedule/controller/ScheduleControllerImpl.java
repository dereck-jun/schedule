package com.example.schedule.controller;

import com.example.schedule.controller.request.CreateScheduleRequest;
import com.example.schedule.controller.request.DeleteScheduleRequest;
import com.example.schedule.controller.request.UpdateScheduleRequest;
import com.example.schedule.controller.response.ApiResponse;
import com.example.schedule.controller.request.ScheduleSearchCond;
import com.example.schedule.service.ScheduleDto;
import com.example.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleControllerImpl implements ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ApiResponse<ScheduleDto> createSchedule(@RequestBody CreateScheduleRequest request) {
        ScheduleDto createdSchedule = scheduleService.save(request);
        return ApiResponse.success(200, createdSchedule, "일정 생성 성공");
    }

    @GetMapping
    public ApiResponse<List<ScheduleDto>> findAllSchedules(@ModelAttribute ScheduleSearchCond cond) {
        List<ScheduleDto> schedules = scheduleService.findSchedulesByAuthorOrSelectedDate(cond);
        return ApiResponse.success(200, schedules, "일정 전체 조회 성공");
    }

    @GetMapping("/{scheduleId}")
    public ApiResponse<ScheduleDto> findScheduleById(@PathVariable("scheduleId") Long id) {
        ScheduleDto findSchedule = scheduleService.findById(id);
        return ApiResponse.success(200, findSchedule, "일정 단건 조회 성공");
    }

    @PatchMapping("/{scheduleId}")
    public ApiResponse<ScheduleDto> updateSchedule(@PathVariable("scheduleId") Long id,
                                                   @RequestBody UpdateScheduleRequest request) {
        ScheduleDto updatedSchedule = scheduleService.update(id, request);
        return ApiResponse.success(200, updatedSchedule, "일정 수정 성공");
    }

    @DeleteMapping("/{scheduleId}")
    public ApiResponse<Void> deleteSchedule(@PathVariable("scheduleId") Long id,
                                            @RequestBody DeleteScheduleRequest request) {
        scheduleService.delete(id, request);
        return ApiResponse.success(200, null, "일정 삭제 성공");
    }
}
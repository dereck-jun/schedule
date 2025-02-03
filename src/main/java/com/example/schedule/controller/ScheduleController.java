package com.example.schedule.controller;

import com.example.schedule.controller.request.CreateScheduleRequest;
import com.example.schedule.controller.request.DeleteScheduleRequest;
import com.example.schedule.controller.request.GetSchedulesRequest;
import com.example.schedule.controller.request.UpdateScheduleRequest;
import com.example.schedule.repository.ScheduleWithAuthor;
import com.example.schedule.service.dto.PageDto;
import com.example.schedule.service.dto.ScheduleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Schedule", description = "Schedule 관련 API 입니다.")
public interface ScheduleController {

    @Operation(summary = "일정 생성", description = "새로운 일정을 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "새로운 일정 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청으로 인해 일정 생성 실패(잘못된 데이터 타입)")})
    com.example.schedule.controller.response.ApiResponse<ScheduleDto> createSchedule(@RequestBody CreateScheduleRequest request);

    @Operation(summary = "일정 목록 조회", description = "작성자명이나 선택한 날짜에 해당하는 일정을 모두 가져옵니다. 조건을 설정하지 않은 경우 모든 결과를 가져옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "일정 전체 조회 성공"),
        @ApiResponse(responseCode = "400", description = "일정 전체 조회 실패(날짜 데이터 포맷 형식)")})
    com.example.schedule.controller.response.ApiResponse<PageDto<ScheduleWithAuthor>> findAllSchedules(@ModelAttribute GetSchedulesRequest request);

    @Operation(summary = "일정 단건 조회", description = "ID 값에 일치하는 일정을 가져옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "일정 단건 조회 성공"),
        @ApiResponse(responseCode = "400", description = "일정 단건 조회 실패(잘못된 ID 데이터 타입이나 값)"),
        @ApiResponse(responseCode = "404", description = "일정 조회 실패(ID 값에 해당하는 일정이 없거나 삭제됨)")})
    com.example.schedule.controller.response.ApiResponse<ScheduleDto> findScheduleById(@PathVariable("scheduleId") Long id);

    @Operation(summary = "일정 수정", description = "ID 값에 일치하는 일정을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "일정 수정 성공"),
        @ApiResponse(responseCode = "400", description = "일정 수정 실패(잘못된 ID 데이터 타입이나 값)"),
        @ApiResponse(responseCode = "404", description = "일정 수정 실패(ID 값에 해당하는 일정이 없거나 삭제됨)")})
    com.example.schedule.controller.response.ApiResponse<ScheduleDto> updateSchedule(@PathVariable("scheduleId") Long id, @RequestBody UpdateScheduleRequest request);

    @Operation(summary = "일정 삭제", description = "ID 값에 일치하는 일정을 (소프트)삭제 합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "일정 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "일정 삭제 실패(잘못된 ID 데이터 타입이나 값)"),
        @ApiResponse(responseCode = "404", description = "일정 삭제 실패(ID 값에 해당하는 일정이 없거나 이미 삭제됨)")})
    com.example.schedule.controller.response.ApiResponse<Void> deleteSchedule(@PathVariable("scheduleId") Long id, @RequestBody DeleteScheduleRequest request);
}

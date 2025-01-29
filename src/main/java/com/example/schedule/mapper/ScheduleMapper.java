package com.example.schedule.mapper;

import com.example.schedule.entity.Schedule;
import com.example.schedule.service.ScheduleDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",  // spring bean 으로 생성
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,  // 생성자 주입
    unmappedTargetPolicy = ReportingPolicy.ERROR    // 매핑 실패 시 컴파일 에러
)
public interface ScheduleMapper {

    @Mapping(target = "scheduleId", source = "id")
    ScheduleDto scheduleToScheduleDto(Schedule schedule);

}
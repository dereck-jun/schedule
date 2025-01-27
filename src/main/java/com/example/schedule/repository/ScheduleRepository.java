package com.example.schedule.repository;

import com.example.schedule.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {

    Schedule save(Long authorId, String todo, String password);

    List<Schedule> findAll(Long authorId, LocalDate selectedDate);

    Optional<Schedule> findByScheduleId(Long scheduleId);

    Schedule update(Long scheduleId, String todo);

    void delete(Long scheduleId);
}

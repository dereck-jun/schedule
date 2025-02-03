package com.example.schedule.repository;

import com.example.schedule.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {

    Schedule save(Long authorId, String todo, String password);

    List<ScheduleWithAuthor> findAll(Long authorId, LocalDate selectedDate, int page, int size);

    Optional<Schedule> findById(Long scheduleId);

    Schedule update(Long scheduleId, String todo);

    void delete(Long scheduleId);

    long count();
}

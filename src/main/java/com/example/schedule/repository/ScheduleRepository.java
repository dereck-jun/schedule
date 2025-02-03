package com.example.schedule.repository;

import com.example.schedule.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {

    Schedule save(String author, String todo, String password);

    List<Schedule> findAll(String author, LocalDate selectedDate);

    Optional<Schedule> findById(Long scheduleId);

    Schedule update(Long scheduleId, String author, String todo);

    void delete(Long scheduleId);
}

package com.example.schedule.service;

import com.example.schedule.entity.Author;
import com.example.schedule.exception.ClientException;
import com.example.schedule.exception.ScheduleNotFoundException;
import com.example.schedule.controller.request.CreateScheduleRequest;
import com.example.schedule.controller.request.DeleteScheduleRequest;
import com.example.schedule.entity.Schedule;
import com.example.schedule.mapper.ScheduleMapper;
import com.example.schedule.repository.AuthorRepository;
import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.controller.request.UpdateScheduleRequest;
import com.example.schedule.controller.request.ScheduleSearchCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.schedule.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final AuthorRepository authorRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ScheduleDto save(CreateScheduleRequest request) {
        Author author = authorRepository.findByAuthor(request.getAuthor())
            .orElseGet(() -> authorRepository.save(request.getAuthor(), request.getEmail()));

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Schedule createdSchedule = scheduleRepository.save(author.getId(), request.getTodo(), encodedPassword);
        return scheduleMapper.scheduleToScheduleDto(createdSchedule);
    }

    @Override
    public List<ScheduleDto> findSchedulesByAuthorOrSelectedDate(ScheduleSearchCond cond) {
        return scheduleRepository.findAll(cond.getAuthorId(), cond.getSelectedDate())
            .stream()
            .map(scheduleMapper::scheduleToScheduleDto)
            .toList();
    }

    @Override
    public ScheduleDto findById(Long scheduleId) {
        Schedule findSchedule = scheduleRepository.findByScheduleId(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND));
        return scheduleMapper.scheduleToScheduleDto(findSchedule);
    }

    @Override
    @Transactional
    public ScheduleDto update(Long scheduleId, UpdateScheduleRequest request) {
        Schedule schedule = scheduleRepository.findByScheduleId(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND));

        Author author = authorRepository.findById(schedule.getAuthorId())
            .orElseThrow(() -> new ScheduleNotFoundException(AUTHOR_NOT_FOUND));

        passwordValidation(request.getPassword(), schedule);

        Schedule updatedSchedule = scheduleRepository.update(scheduleId, request.getTodo());
        authorRepository.update(author.getAuthor());
        return scheduleMapper.scheduleToScheduleDto(updatedSchedule);
    }

    @Override
    @Transactional
    public void delete(Long scheduleId, DeleteScheduleRequest request) {
        Schedule findSchedule = scheduleRepository.findByScheduleId(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND));

        passwordValidation(request.getPassword(), findSchedule);

        scheduleRepository.delete(scheduleId);
    }

    private void passwordValidation(String password, Schedule schedule) {
        if (!passwordEncoder.matches(password, schedule.getPassword())) {
            throw new ClientException(INVALID_PASSWORD);
        }
    }
}

package com.example.schedule.service;

import com.example.schedule.exception.ClientException;
import com.example.schedule.exception.ScheduleNotFoundException;
import com.example.schedule.controller.request.CreateScheduleRequest;
import com.example.schedule.controller.request.DeleteScheduleRequest;
import com.example.schedule.entity.Schedule;
import com.example.schedule.mapper.ScheduleMapper;
import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.controller.request.UpdateScheduleRequest;
import com.example.schedule.controller.request.ScheduleSearchCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.schedule.exception.ErrorCode.INVALID_PASSWORD;
import static com.example.schedule.exception.ErrorCode.SCHEDULE_NOT_FOUND;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    @Transactional
    public ScheduleDto save(CreateScheduleRequest request) {
        Schedule createdSchedule = scheduleRepository.save(request.getAuthor(), request.getTodo(), request.getPassword());
        return scheduleMapper.scheduleToScheduleDto(createdSchedule);
    }

    @Override
    public List<ScheduleDto> findSchedulesByAuthorOrSelectedDate(ScheduleSearchCond cond) {
        return scheduleRepository.findAll(cond.getAuthor(), cond.getSelectedDate())
            .stream()
            .map(scheduleMapper::scheduleToScheduleDto)
            .toList();
    }

    @Override
    public ScheduleDto findById(Long scheduleId) {
        Schedule findSchedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND));
        return scheduleMapper.scheduleToScheduleDto(findSchedule);
    }

    @Override
    @Transactional
    public ScheduleDto update(Long scheduleId, UpdateScheduleRequest dto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND));

        if (!schedule.getPassword().equals(dto.getPassword())) {
            throw new ClientException(INVALID_PASSWORD);
        }

        Schedule updatedSchedule = scheduleRepository.update(scheduleId, dto.getAuthor(), dto.getTodo());
        return scheduleMapper.scheduleToScheduleDto(updatedSchedule);
    }

    @Override
    @Transactional
    public void delete(Long scheduleId, DeleteScheduleRequest request) {
        Schedule findSchedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND));

        if (!findSchedule.getPassword().equals(request.getPassword())) {
            throw new ClientException(INVALID_PASSWORD);
        }

        scheduleRepository.delete(scheduleId);
    }
}

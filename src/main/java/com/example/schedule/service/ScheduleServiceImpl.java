package com.example.schedule.service;

import com.example.schedule.controller.request.CreateScheduleRequest;
import com.example.schedule.controller.request.DeleteScheduleRequest;
import com.example.schedule.controller.request.GetSchedulesRequest;
import com.example.schedule.controller.request.UpdateScheduleRequest;
import com.example.schedule.entity.Author;
import com.example.schedule.entity.Schedule;
import com.example.schedule.exception.ClientException;
import com.example.schedule.exception.ScheduleNotFoundException;
import com.example.schedule.mapper.ScheduleMapper;
import com.example.schedule.repository.AuthorRepository;
import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.repository.ScheduleWithAuthor;
import com.example.schedule.service.dto.PageDto;
import com.example.schedule.service.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        Optional<Author> findAuthor = authorRepository.findByEmail(request.getName(), request.getEmail());
        if (findAuthor.isPresent()) {
            return createSchedule(findAuthor.get(), request);
        }

        if (authorRepository.existsByName(request.getName())) {
            throw new ClientException("name", AUTHOR_NAME_DUPLICATED);
        } else if (authorRepository.existsByEmail(request.getEmail())) {
            throw new ClientException("email", EMAIL_DUPLICATED);
        }

        Author newAuthor = authorRepository.save(request.getName(), request.getEmail());
        return createSchedule(newAuthor, request);
    }

    @Override
    public PageDto<ScheduleWithAuthor> findAll(GetSchedulesRequest request) {
        List<ScheduleWithAuthor> resultList = scheduleRepository.findAll(request.getAuthorId(), request.getSelectedDate(), request.getPage(), request.getSize());

        long totalCount = scheduleRepository.count();
        int totalPages = (int) Math.ceil((double) totalCount / request.getSize());
        return new PageDto<>(resultList, request.getPage(), request.getSize(), totalCount, totalPages);
    }

    @Override
    public ScheduleDto findById(Long scheduleId) {
        Schedule findSchedule = getScheduleOrThrowException(scheduleId);
        return scheduleMapper.scheduleToScheduleDto(findSchedule);
    }

    @Override
    @Transactional
    public ScheduleDto update(Long scheduleId, UpdateScheduleRequest request) {
        Schedule schedule = getScheduleOrThrowException(scheduleId);

        Author author = authorRepository.findById(schedule.getAuthorId())
            .orElseThrow(() -> new ScheduleNotFoundException(AUTHOR_NOT_FOUND));

        passwordCheck(request.getPassword(), schedule);

        if (authorRepository.existsByName(request.getName())) {
            throw new ClientException("name", AUTHOR_NAME_DUPLICATED);
        }

        authorRepository.update(author.getId(), request.getName());
        Schedule updatedSchedule = scheduleRepository.update(scheduleId, request.getTodo());
        return scheduleMapper.scheduleToScheduleDto(updatedSchedule);
    }

    @Override
    @Transactional
    public void delete(Long scheduleId, DeleteScheduleRequest request) {
        Schedule findSchedule = getScheduleOrThrowException(scheduleId);

        passwordCheck(request.getPassword(), findSchedule);

        scheduleRepository.delete(scheduleId);
    }

    private ScheduleDto createSchedule(Author author, CreateScheduleRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Schedule createdSchedule = scheduleRepository.save(author.getId(), request.getTodo(), encodedPassword);
        return scheduleMapper.scheduleToScheduleDto(createdSchedule);
    }

    private Schedule getScheduleOrThrowException(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND));
    }

    private void passwordCheck(String password, Schedule schedule) {
        if (!passwordEncoder.matches(password, schedule.getPassword())) {
            throw new ClientException("password", INVALID_PASSWORD);
        }
    }
}

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
        // TODO: 중복 검사를 실행할 수 있는지? -> 로그인 기능이 없으니 유저를 솎아낼 수 없다고 생각함
        // Why? 보통 로그인을 실행한 뒤에 메인 기능들을 사용할 수 있지만 현재 로그인 기능이 없기 때문에 유저의 유무를 확인해도 의미가 없음
        // 유저가 있으면 password 만을 다르게 하여 일정을 잠궈놓기 때문임
        Author author = authorRepository.findAuthorByNameOrEmail(request.getName(), request.getEmail())
            .orElseGet(() -> authorRepository.save(request.getName(), request.getEmail()));

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Schedule createdSchedule = scheduleRepository.save(author.getId(), request.getTodo(), encodedPassword);
        return scheduleMapper.scheduleToScheduleDto(createdSchedule);
    }

    @Override
    public PageDto<ScheduleWithAuthor> findAll(GetSchedulesRequest request) {
        List<ScheduleWithAuthor> dtos = scheduleRepository.findAll(request.getAuthorId(), request.getSelectedDate(), request.getPage(), request.getSize());

        long totalCount = scheduleRepository.count();
        int totalPages = (int) Math.ceil((double) totalCount / request.getSize());
        return new PageDto<>(dtos, request.getPage(), request.getSize(), totalCount, totalPages);
    }

    @Override
    public ScheduleDto findById(Long scheduleId) {
        Schedule findSchedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND));
        return scheduleMapper.scheduleToScheduleDto(findSchedule);
    }

    @Override
    @Transactional
    public ScheduleDto update(Long scheduleId, UpdateScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND));

        Author author = authorRepository.findById(schedule.getAuthorId())
            .orElseThrow(() -> new ScheduleNotFoundException(AUTHOR_NOT_FOUND));

        passwordCheck(request.getPassword(), schedule);

        Optional<Author> authorByName = authorRepository.findAuthorByName(request.getName());
        if (authorByName.isPresent()) {
            throw new ClientException(AUTHOR_NAME_DUPLICATED);
        }

        authorRepository.update(author.getId(), request.getName());
        Schedule updatedSchedule = scheduleRepository.update(scheduleId, request.getTodo());
        return scheduleMapper.scheduleToScheduleDto(updatedSchedule);
    }

    @Override
    @Transactional
    public void delete(Long scheduleId, DeleteScheduleRequest request) {
        Schedule findSchedule = scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND));

        passwordCheck(request.getPassword(), findSchedule);

        scheduleRepository.delete(scheduleId);
    }

    private void passwordCheck(String password, Schedule schedule) {
        if (!passwordEncoder.matches(password, schedule.getPassword())) {
            throw new ClientException(INVALID_PASSWORD);
        }
    }
}

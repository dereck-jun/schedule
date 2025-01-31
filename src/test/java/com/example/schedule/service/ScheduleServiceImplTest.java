package com.example.schedule.service;

import com.example.schedule.controller.request.CreateScheduleRequest;
import com.example.schedule.entity.Author;
import com.example.schedule.entity.Schedule;
import com.example.schedule.mapper.ScheduleMapper;
import com.example.schedule.repository.AuthorRepository;
import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.service.dto.ScheduleDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    @Mock private ScheduleRepository scheduleRepository;
    @Mock private AuthorRepository authorRepository;
    @Mock private ScheduleMapper scheduleMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @Test
    public void 일정_생성() {
        // given
        LocalDateTime now = LocalDateTime.now();
        CreateScheduleRequest request = new CreateScheduleRequest("tester", "tester@test.com", "오늘 할 일", "password");
        Author author = new Author(1L, "tester", "tester@test.com", now, now, true);


        // when
        when(authorRepository.findByEmail(request.getName(), request.getEmail())).thenReturn(Optional.of(author));
        when(scheduleRepository.save(anyLong(), anyString(), anyString())).thenReturn(new Schedule());
        when(scheduleMapper.scheduleToScheduleDto(any())).thenReturn(new ScheduleDto());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        ScheduleDto scheduleDto = scheduleService.save(request);

        // then
        assertNotNull(scheduleDto);
        verify(authorRepository, times(1))
            .findByEmail("tester", "tester@test.com");
        verify(scheduleRepository, times(1))
            .save(eq(author.getId()), eq("오늘 할 일"), eq("encodedPassword"));
    }


}
package com.example.schedule.config;

import com.example.schedule.repository.ScheduleRepository;
import com.example.schedule.repository.ScheduleRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcTemplateConfig {
    @Bean
    public ScheduleRepository scheduleRepository(DataSource dataSource) {
        return new ScheduleRepositoryImpl(dataSource);
    }
}

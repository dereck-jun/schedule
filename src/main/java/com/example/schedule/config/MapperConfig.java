package com.example.schedule.config;

import com.example.schedule.mapper.ScheduleMapper;
import com.example.schedule.mapper.ScheduleMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ScheduleMapper scheduleMapper() {
        return new ScheduleMapperImpl();
    }
}

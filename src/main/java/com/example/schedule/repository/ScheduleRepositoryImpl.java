package com.example.schedule.repository;

import com.example.schedule.entity.Schedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ScheduleRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("schedules")
            .usingGeneratedKeyColumns("schedule_id");
    }


    @Override
    public Schedule save(String author, String todo, String password) {
        final LocalDateTime now = LocalDateTime.now();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("author", author)
            .addValue("todo", todo)
            .addValue("password", password)
            .addValue("created_at", now)
            .addValue("last_updated", now)
            .addValue("is_active", true);

        Number key = jdbcInsert.executeAndReturnKey(params);

        return Schedule.builder()
            .id(key.longValue())
            .author(author)
            .todo(todo)
            .password(password)
            .createdAt(now)
            .lastUpdated(now)
            .build();
    }

    @Override
    public List<Schedule> findAll(String author, LocalDate selectedDate) {
        String sql = "select * from schedules where is_active = true";

        if (StringUtils.hasText(author)) {
            sql += " and author like concat('%', '" + author + "', '%')";

        }

        if (selectedDate != null) {
            sql += " and last_updated between '" + selectedDate + "' and date_add('" + selectedDate + "', interval '23:59:59' hour_second)";
        }

        sql += " order by last_updated desc";

        return jdbcTemplate.query(sql, scheduleRowMapper());
    }

    @Override
    public Optional<Schedule> findById(Long scheduleId) {
        String sql = "select * from schedules where schedule_id = :scheduleId and is_active = true";
        SqlParameterSource param = new MapSqlParameterSource("scheduleId", scheduleId);

        List<Schedule> schedules = jdbcTemplate.query(sql, param, scheduleRowMapper());
        return schedules.stream().findFirst();
    }

    @Override
    public Schedule update(Long scheduleId, String author, String todo) {
        String sql = "update schedules set";

        if (!StringUtils.hasText(author) && StringUtils.hasText(todo)) {
            throw new RuntimeException("변경할 값을 입력해주세요");
        }

        boolean commaFlag = false;
        if (StringUtils.hasText(author)) {
            sql += " author = '" + author + "'";
            commaFlag = true;
        }

        if (StringUtils.hasText(todo)) {
            if (commaFlag) {
                sql += ",";
            }
            sql += " todo = '" + todo + "'";
        }
        sql += ", last_updated = '" + LocalDateTime.now() + "' where schedule_id = :scheduleId and is_active = true";

        SqlParameterSource params = new MapSqlParameterSource("scheduleId", scheduleId);
        jdbcTemplate.update(sql, params);

        sql = "select * from schedules where schedule_id = :scheduleId and is_active = true";
        SqlParameterSource param = new MapSqlParameterSource("scheduleId", scheduleId);

        return jdbcTemplate.queryForObject(sql, param, scheduleRowMapper());
    }

    @Override
    public void delete(Long scheduleId) {
        String sql = "update schedules set is_active = false where schedule_id = :scheduleId";

        SqlParameterSource param = new MapSqlParameterSource("scheduleId", scheduleId);
        jdbcTemplate.update(sql, param);
    }

    private RowMapper<Schedule> scheduleRowMapper() {
        return (rs, rowNum) ->
            Schedule.builder()
                .id(rs.getLong("schedule_id"))
                .author(rs.getString("author"))
                .todo(rs.getString("todo"))
                .password(rs.getString("password"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .lastUpdated(rs.getTimestamp("last_updated").toLocalDateTime())
                .build();
    }

}

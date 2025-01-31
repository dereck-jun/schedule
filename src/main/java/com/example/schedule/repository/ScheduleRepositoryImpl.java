package com.example.schedule.repository;

import com.example.schedule.entity.Schedule;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public Schedule save(Long authorId, String todo, String password) {
        LocalDateTime now = LocalDateTime.now();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("authorId", authorId)
            .addValue("todo", todo)
            .addValue("password", password)
            .addValue("created_at", now)
            .addValue("last_updated", now)
            .addValue("is_active", true);

        Number key = jdbcInsert.executeAndReturnKey(params);

        return Schedule.builder()
            .id(key.longValue())
            .authorId(authorId)
            .todo(todo)
            .password(password)
            .createdAt(now)
            .lastUpdated(now)
            .build();
    }

    @Override
    public List<ScheduleWithAuthor> findAll(Long authorId, LocalDate selectedDate, int page, int size) {
        int offset = (page - 1) * size;
        String sql = "select * from schedules s inner join authors a on s.author_id = a.author_id where s.is_active = true and a.is_active = true";
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (authorId != null) {
            sql += " and a.author_id = :authorId";
            params.addValue("authorId", authorId);
        }

        if (selectedDate != null) {
            sql += " and s.last_updated between '" + selectedDate + "' and date_add('" + selectedDate + "', interval '23:59:59' hour_second)";
        }

        sql += " order by s.last_updated desc";

        sql += " limit :size offset :offset";
        params.addValue("size", size)
            .addValue("offset", offset);

        return jdbcTemplate.query(sql, params, (rs, rowNum) ->
            ScheduleWithAuthor.builder()
                .scheduleId(rs.getLong("schedule_id"))
                .todo(rs.getString("todo"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .lastUpdated(rs.getTimestamp("last_updated").toLocalDateTime())
                .name(rs.getString("name"))
                .build()
        );
    }

    @Override
    public long count() {
        String sql = "select count(*) from schedules s inner join authors a on s.author_id = a.author_id where s.is_active = true and a.is_active = true";
        Long count = jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Long.class);
        return count != null ? count : 0;
    }

    @Override
    public Optional<Schedule> findById(Long scheduleId) {
        String sql = "select * from schedules where schedule_id = :scheduleId and is_active = true";
        SqlParameterSource param = new MapSqlParameterSource("scheduleId", scheduleId);

        List<Schedule> schedules = jdbcTemplate.query(sql, param, scheduleRowMapper());
        return schedules.stream().findFirst();
    }

    @Override
    public Schedule update(Long scheduleId, String todo) {
        String sql = "update schedules set todo = :todo, last_updated = '" + LocalDateTime.now() + "' where schedule_id = :scheduleId and is_active = true";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("todo", todo)
            .addValue("scheduleId", scheduleId);

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
                .authorId(rs.getLong("author_id"))
                .todo(rs.getString("todo"))
                .password(rs.getString("password"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .lastUpdated(rs.getTimestamp("last_updated").toLocalDateTime())
                .build();
    }

}

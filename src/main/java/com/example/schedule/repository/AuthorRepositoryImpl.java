package com.example.schedule.repository;

import com.example.schedule.entity.Author;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public AuthorRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("authors")
            .usingGeneratedKeyColumns("author_id");
    }

    @Override
    public Author save(String name, String email) {
        LocalDateTime now = LocalDateTime.now();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name)
            .addValue("email", email)
            .addValue("created_at", now)
            .addValue("last_updated", now)
            .addValue("is_active", true);

        Number key = jdbcInsert.executeAndReturnKey(params);

        return Author.builder()
            .id(key.longValue())
            .name(name)
            .email(email)
            .createdAt(now)
            .lastUpdated(now)
            .build();
    }

    @Override
    public void update(Long authorId, String name) {
        String sql = "update authors set name = :name, last_updated = '" + LocalDateTime.now() + "' where author_id = :authorId and is_active = true";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name)
            .addValue("authorId", authorId);

        jdbcTemplate.update(sql, params);

        sql = "select * from authors where name = :name and is_active = true";

        jdbcTemplate.queryForObject(sql, params, authorRowMapper());
    }

    @Override
    public Optional<Author> findById(Long authorId) {
        String sql = "select * from authors where author_id = :authorId";

        SqlParameterSource params = new MapSqlParameterSource("authorId", authorId);

        List<Author> authors = jdbcTemplate.query(sql, params, authorRowMapper());

        return authors.stream().findFirst();
    }

    @Override
    public Optional<Author> findByEmail(String name, String email) {
        String sql = "select * from authors where name = :name and email = :email and is_active = true";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name)
            .addValue("email", email);

        return jdbcTemplate.query(sql, params, authorRowMapper()).stream().findFirst();
    }

    @Override
    public boolean existsByName(String name) {
        String sql = "select IF(count(*) > 0, true, false) from authors where name = :name and is_active = true";
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "select IF(count(*) > 0, true, false) from authors where email = :email and is_active = true";
        MapSqlParameterSource params = new MapSqlParameterSource("email", email);
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }

    private RowMapper<Author> authorRowMapper() {
        return (rs, rowNum) ->
            Author.builder()
                .id(rs.getLong("author_id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .lastUpdated(rs.getTimestamp("last_updated").toLocalDateTime())
                .build();
    }
}

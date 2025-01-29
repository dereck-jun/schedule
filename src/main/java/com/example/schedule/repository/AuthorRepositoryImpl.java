package com.example.schedule.repository;

import com.example.schedule.entity.Author;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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

    private static RowMapper<Author> authorRowMapper() {
        return (rs, rowNum) ->
            Author.builder()
                .id(rs.getLong("author_id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .lastUpdated(rs.getTimestamp("last_updated").toLocalDateTime())
                .build();
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
    public Author update(Long authorId, String name) {
        String sql = "update authors set name = :name, last_updated = '" + LocalDateTime.now() + "'";
        sql += " where author_id = :authorId and is_active = true";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name)
            .addValue("authorId", authorId);

        jdbcTemplate.update(sql, params);

        sql = "select * from authors where name = :name and is_active = true";

        return jdbcTemplate.queryForObject(sql, params, authorRowMapper());
    }

    @Override
    public Optional<Author> findById(Long authorId) {
        String sql = "select * from authors where author_id = :authorId";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("authorId", authorId);

        List<Author> authors = jdbcTemplate.query(sql, params, authorRowMapper());

        return authors.stream().findFirst();
    }

    @Override
    public Optional<Author> findAuthorByName(String name) {
        String sql = "select * from authors where name = :name";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);

        List<Author> authors = jdbcTemplate.query(sql, params, authorRowMapper());

        return authors.stream().findFirst();
    }

    @Override
    public Optional<Author> findAuthorByEmail(String email) {
        String sql = "select * from authors where email = :email and is_active = true";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", email);

        List<Author> authors = jdbcTemplate.query(sql, params, authorRowMapper());

        return authors.stream().findFirst();
    }

    @Override
    public Optional<Author> findAuthorByNameOrEmail(String name, String email) {
        String sql = "select * from authors where name = :name or email = :email and is_active = true";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name)
            .addValue("email", email);

        return jdbcTemplate.query(sql, params, authorRowMapper()).stream().findFirst();
    }
}

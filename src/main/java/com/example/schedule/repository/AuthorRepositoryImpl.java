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

    @Override
    public Author save(String author, String email) {
        LocalDateTime now = LocalDateTime.now();

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("author", author)
            .addValue("email", email)
            .addValue("created_at", now)
            .addValue("last_updated", now)
            .addValue("is_active", true);

        Number key = jdbcInsert.executeAndReturnKey(params);

        return Author.builder()
            .id(key.longValue())
            .author(author)
            .email(email)
            .createdAt(now)
            .lastUpdated(now)
            .build();
    }

    @Override
    public Author update(String author) {
        String sql = "update authors set author = :author";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("author", author);

        jdbcTemplate.update(sql, params);

        sql = "select * from authors where author = :author and is_active = true";

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
    public Optional<Author> findByAuthor(String author) {
        String sql = "select * from authors where author = :author";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("author", author);

        List<Author> authors = jdbcTemplate.query(sql, params, authorRowMapper());

        return authors.stream().findFirst();
    }

    private static RowMapper<Author> authorRowMapper() {
        return (rs, rowNum) ->
            Author.builder()
                .id(rs.getLong("author_id"))
                .author(rs.getString("author"))
                .email(rs.getString("email"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .lastUpdated(rs.getTimestamp("last_updated").toLocalDateTime())
                .build();
    }
}

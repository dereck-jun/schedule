package com.example.schedule.repository;

import com.example.schedule.entity.Author;

import java.util.Optional;

public interface AuthorRepository {

    Author save(String name, String email);

    void update(Long authorId, String name);

    Optional<Author> findById(Long authorId);

    Optional<Author> findByEmail(String name, String email);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}

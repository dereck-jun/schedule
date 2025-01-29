package com.example.schedule.repository;

import com.example.schedule.entity.Author;

import java.util.Optional;

public interface AuthorRepository {

    Author save(String name, String email);

    Author update(Long authorId, String name);

    Optional<Author> findById(Long authorId);

    Optional<Author> findAuthorByName(String name);

    Optional<Author> findAuthorByEmail(String email);

    Optional<Author> findAuthorByNameOrEmail(String name, String email);
}

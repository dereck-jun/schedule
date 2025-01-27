package com.example.schedule.repository;

import com.example.schedule.entity.Author;

import java.util.Optional;

public interface AuthorRepository {

    Author save(String author, String email);

    Author update(String author);

    Optional<Author> findById(Long authorId);

    Optional<Author> findByAuthor(String author);
}

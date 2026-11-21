package com.bcphoenix.repository;

import com.bcphoenix.model.News;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findAllByPublishedTrueOrderByPublishedAtDesc();

    List<News> findAllByPublishedTrueOrderByPublishedAtDesc(
        Pageable pageable
    );

    Optional<News> findBySlugAndPublishedTrue(String slug);

    List<News> findAllByOrderByCreatedAtDesc();

    boolean existsBySlug(String slug);
}
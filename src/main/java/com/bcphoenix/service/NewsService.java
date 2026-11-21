package com.bcphoenix.service;

import com.bcphoenix.dto.request.NewsRequest;
import com.bcphoenix.dto.response.NewsResponse;
import com.bcphoenix.dto.response.NewsSummaryResponse;
import com.bcphoenix.model.News;
import com.bcphoenix.repository.NewsRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class NewsService {

    private final NewsRepository repository;

    public NewsService(NewsRepository repository) {
        this.repository = repository;
    }

    // ================================
    // PUBLIC
    // ================================

    @Transactional(readOnly = true)
    public List<NewsSummaryResponse> getPublishedNews(Integer limit) {

        List<News> news = limit == null
            ? repository.findAllByPublishedTrueOrderByPublishedAtDesc()
            : repository.findAllByPublishedTrueOrderByPublishedAtDesc(
                PageRequest.of(0, Math.min(Math.max(limit, 1), 100))
            );

        return news.stream()
            .map(this::toSummary)
            .toList();
    }

    @Transactional(readOnly = true)
    public NewsResponse getPublishedNewsBySlug(String slug) {

        News news = repository.findBySlugAndPublishedTrue(slug)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Actualité introuvable"
            ));

        return toResponse(news);
    }

    // ================================
    // ADMIN
    // ================================

    @Transactional(readOnly = true)
    public List<NewsResponse> getAllNews() {

        return repository.findAllByOrderByCreatedAtDesc()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public NewsResponse getNewsById(Long id) {
        return toResponse(findById(id));
    }

    public NewsResponse createNews(NewsRequest request) {

        News news = new News();

        news.setTitle(request.title());
        news.setSlug(generateUniqueSlug(request.title()));
        news.setSummary(request.summary());
        news.setContent(request.content());
        news.setCoverImage(request.coverImage());
        news.setPublished(request.published());

        if (request.published()) {
            news.setPublishedAt(LocalDateTime.now());
        }

        return toResponse(repository.save(news));
    }

    public NewsResponse updateNews(Long id, NewsRequest request) {

        News news = findById(id);

        news.setTitle(request.title());
        news.setSummary(request.summary());
        news.setContent(request.content());
        news.setCoverImage(request.coverImage());

        if (request.published() && !news.isPublished()) {
            news.setPublishedAt(LocalDateTime.now());
        }

        if (!request.published()) {
            news.setPublishedAt(null);
        }

        news.setPublished(request.published());

        return toResponse(repository.save(news));
    }

    public void deleteNews(Long id) {
        repository.delete(findById(id));
    }

    // ================================
    // HELPERS
    // ================================

    private News findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Actualité introuvable"
            ));
    }

    private String generateUniqueSlug(String title) {

        String base = Normalizer.normalize(
                title,
                Normalizer.Form.NFD
            )
            .replaceAll("\\p{M}+", "")
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("^-|-$", "");

        if (base.isBlank()) {
            base = "actualite";
        }

        String slug = base;
        int suffix = 2;

        while (repository.existsBySlug(slug)) {
            slug = base + "-" + suffix++;
        }

        return slug;
    }

    private NewsResponse toResponse(News news) {

        return new NewsResponse(
            news.getId(),
            news.getTitle(),
            news.getSlug(),
            news.getSummary(),
            news.getContent(),
            news.getCoverImage(),
            news.isPublished(),
            news.getCreatedAt(),
            news.getUpdatedAt(),
            news.getPublishedAt()
        );
    }

    private NewsSummaryResponse toSummary(News news) {

        return new NewsSummaryResponse(
            news.getId(),
            news.getTitle(),
            news.getSlug(),
            news.getSummary(),
            news.getCoverImage(),
            news.getPublishedAt()
        );
    }
}
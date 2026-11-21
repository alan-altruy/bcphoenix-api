package com.bcphoenix.controller.admin;

import com.bcphoenix.dto.request.NewsRequest;
import com.bcphoenix.dto.response.NewsResponse;
import com.bcphoenix.service.NewsService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/news")
public class AdminNewsController {

    private final NewsService service;

    public AdminNewsController(NewsService service) {
        this.service = service;
    }

    @GetMapping
    public List<NewsResponse> getAllNews() {
        return service.getAllNews();
    }

    @GetMapping("/{id}")
    public NewsResponse getNews(@PathVariable Long id) {
        return service.getNewsById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewsResponse createNews(
        @Valid @RequestBody NewsRequest request
    ) {
        return service.createNews(request);
    }

    @PutMapping("/{id}")
    public NewsResponse updateNews(
        @PathVariable Long id,
        @Valid @RequestBody NewsRequest request
    ) {
        return service.updateNews(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNews(@PathVariable Long id) {
        service.deleteNews(id);
    }
}
package com.bcphoenix.controller.visitor;

import com.bcphoenix.dto.response.NewsResponse;
import com.bcphoenix.dto.response.NewsSummaryResponse;
import com.bcphoenix.service.NewsService;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService service;

    public NewsController(NewsService service) {
        this.service = service;
    }

    @GetMapping
    public List<NewsSummaryResponse> getNews(
        @RequestParam(required = false) Integer limit
    ) {
        return service.getPublishedNews(limit);
    }

    @GetMapping("/{slug}")
    public NewsResponse getNewsBySlug(@PathVariable String slug) {
        return service.getPublishedNewsBySlug(slug);
    }
}
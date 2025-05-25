package com.bmilab.backend.domain.project.controller;

import com.bmilab.backend.domain.project.dto.response.RSSResponse;
import com.bmilab.backend.domain.project.service.RSSService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects/rss")
@RequiredArgsConstructor
public class RSSController implements RSSApi {
    private final RSSService rssService;

    @GetMapping
    public ResponseEntity<RSSResponse> getAllRssAssignments(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long minBudget,
            @RequestParam(required = false) Long maxBudget
    ) {
        return ResponseEntity.ok(rssService.getAllRssAssignments(pageNo, size, search, minBudget, maxBudget));
    }
}

package com.example.api.controller;

import com.example.dtos.RatingDto;
import com.example.services.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/recette/{id}/rating")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) { this.ratingService = ratingService; }

    @GetMapping
    public RatingDto getRating(@PathVariable Integer id) {
        return ratingService.getRating(id, getAuthenticatedUserId());
    }

    @PostMapping
    public ResponseEntity<RatingDto> submitRating(@PathVariable Integer id,
                                                   @RequestBody Map<String, Integer> body) {
        Long userId = getAuthenticatedUserId();
        if (userId == null) return ResponseEntity.status(401).build();
        Integer rating = body.get("rating");
        if (rating == null || rating < 1 || rating > 5) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(ratingService.submitRating(id, userId, rating));
    }

    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) return null;
        return (Long) auth.getPrincipal();
    }
}

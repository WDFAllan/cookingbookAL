package com.example.services;

import com.example.dtos.RatingDto;
import com.example.port.RatingPort;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private final RatingPort ratingPort;

    public RatingService(RatingPort ratingPort) { this.ratingPort = ratingPort; }

    public RatingDto submitRating(Integer recipeId, Long userId, int rating) {
        return ratingPort.submitRating(recipeId, userId, rating);
    }

    public RatingDto getRating(Integer recipeId, Long userId) {
        return ratingPort.getRating(recipeId, userId);
    }
}

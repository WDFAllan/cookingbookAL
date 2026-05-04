package com.example.port;

import com.example.dtos.RatingDto;

public interface RatingPort {
    RatingDto submitRating(Integer recipeId, Long userId, int rating);
    RatingDto getRating(Integer recipeId, Long userId);
}

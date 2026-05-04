package com.example.adapters;

import com.example.dtos.RatingDto;
import com.example.model.RecipeRating;
import com.example.port.RatingPort;
import com.example.repository.RatingRepository;
import com.example.repository.RecetteRepository;
import org.springframework.stereotype.Component;

@Component
public class RatingAdapter implements RatingPort {

    private final RatingRepository ratingRepo;
    private final RecetteRepository recetteRepo;

    public RatingAdapter(RatingRepository ratingRepo, RecetteRepository recetteRepo) {
        this.ratingRepo = ratingRepo;
        this.recetteRepo = recetteRepo;
    }

    @Override
    public RatingDto submitRating(Integer recipeId, Long userId, int rating) {
        RecipeRating entity = ratingRepo.findByRecipeIdAndUserId(recipeId, userId)
                .orElseGet(() -> { RecipeRating r = new RecipeRating(); r.setRecipeId(recipeId); r.setUserId(userId); return r; });
        entity.setRating(rating);
        ratingRepo.save(entity);

        double avg = ratingRepo.findAverageByRecipeId(recipeId);
        long count = ratingRepo.countByRecipeId(recipeId);

        recetteRepo.findById(recipeId).ifPresent(r -> { r.setRate(avg); recetteRepo.save(r); });

        return new RatingDto(avg, count, rating);
    }

    @Override
    public RatingDto getRating(Integer recipeId, Long userId) {
        double avg = ratingRepo.findAverageByRecipeId(recipeId);
        long count = ratingRepo.countByRecipeId(recipeId);
        Integer userRating = userId == null ? null :
                ratingRepo.findByRecipeIdAndUserId(recipeId, userId)
                        .map(RecipeRating::getRating).orElse(null);
        return new RatingDto(avg, count, userRating);
    }
}

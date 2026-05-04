package com.example.repository;

import com.example.model.RecipeRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<RecipeRating, Long> {

    Optional<RecipeRating> findByRecipeIdAndUserId(Integer recipeId, Long userId);

    long countByRecipeId(Integer recipeId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM RecipeRating r WHERE r.recipeId = :recipeId")
    double findAverageByRecipeId(@Param("recipeId") Integer recipeId);
}

package com.example.adapters;

import com.example.dtos.IngredientDto;
import com.example.mapper.IngredientMapper;
import com.example.model.Ingredient;
import com.example.model.Recette;
import com.example.port.IngredientPort;
import com.example.repository.IngredientRepository;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class IngredientAdapter implements IngredientPort {

    private final IngredientMapper mapper;
    private final IngredientRepository repo;
    public IngredientAdapter(IngredientMapper mapper, IngredientRepository repo) {
        this.mapper = mapper;
        this.repo = repo;
    }

    @Override
    public List<IngredientDto> getAllIngredients() {
        List<Ingredient> ingredients = repo.findAll();

        return mapper.map(ingredients);
    }

    @Override
    public IngredientDto save(IngredientDto ingredientsDto) {
        Ingredient ingredientTocreate = mapper.map(ingredientsDto);
        Ingredient ingredient = repo.save(ingredientTocreate);
        return mapper.map(ingredient);
    }
}

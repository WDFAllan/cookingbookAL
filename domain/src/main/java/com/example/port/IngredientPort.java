package com.example.port;

import com.example.dtos.IngredientDto;

import java.util.List;

public interface IngredientPort {

    List<IngredientDto> getAllIngredients();

    IngredientDto save(IngredientDto ingredients);

}

package com.example.mapper;

import com.example.dtos.IngredientDto;
import com.example.model.Ingredient;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    IngredientDto map(Ingredient ingredient);

    List<IngredientDto> map(List<Ingredient> ingredients);

    Ingredient map(IngredientDto ingredientDto);

}

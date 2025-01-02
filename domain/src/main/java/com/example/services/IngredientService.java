package com.example.services;

import com.example.dtos.IngredientDto;
import com.example.port.IngredientPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientPort ingredientPort;

    public IngredientService(IngredientPort ingredientPort) {this.ingredientPort = ingredientPort;}

    public List<IngredientDto> getAllIngredients() {
        return ingredientPort.getAllIngredients();
    }

    public void addIngredient(List<IngredientDto> ingredientDto) {
        for (IngredientDto ingredient : ingredientDto) {
            ingredientPort.save(ingredient);
        }
    }

}

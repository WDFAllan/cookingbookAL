package com.example.api.controller;

import com.example.dtos.IngredientDto;
import com.example.services.IngredientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {this.ingredientService = ingredientService;}

    @GetMapping
    public List<IngredientDto> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @PostMapping
    public void addIngredient(@RequestBody List<IngredientDto> ingredientDto) {
        ingredientService.addIngredient(ingredientDto);
    }

}

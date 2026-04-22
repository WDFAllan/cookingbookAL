package com.example.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecetteDto {

    private Integer id;

    @NotBlank(message = "Le nom de la recette est obligatoire")
    private String name;

    private String imageUrl;
    private Integer prepTime;

    @Min(value = 0, message = "La note doit être entre 0 et 5")
    @Max(value = 5, message = "La note doit être entre 0 et 5")
    private Double rate;

    private LocalDate date;
    private List<String> tags = new ArrayList<>();

    @NotEmpty(message = "La recette doit contenir au moins un ingrédient")
    @Valid
    private List<IngredientDto> ingredients = new ArrayList<>();

    @NotEmpty(message = "La recette doit contenir au moins une étape")
    private List<String> steps = new ArrayList<>();


    public RecetteDto(String name, Double rate, List<String> tags, List<IngredientDto> ingredients, List<String> steps) {

        setName(name);
        setRate(rate);
        setDate(LocalDate.now());
        setTags(tags);
        setIngredients(ingredients);
        setSteps(steps);
    }

    public RecetteDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<String> getTags() {return tags;}

    public void setTags(List<String> tags) {this.tags = tags;}

    public List<IngredientDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientDto> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }
}

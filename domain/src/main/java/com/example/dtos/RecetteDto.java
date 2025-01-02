package com.example.dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecetteDto {

    private int id;
    private String name;
    private float rate;
    private LocalDate date;
    private List<String> tags = new ArrayList<>();
    private List<IngredientDto> ingredients = new ArrayList<>();
    private List<String> steps = new ArrayList<>();


    public RecetteDto(String name, float rate, List<String> tags, List<IngredientDto> ingredients, List<String> steps) {

        setName(name);
        setRate(rate);
        setDate(LocalDate.now());
        setTags(tags);
        setIngredients(ingredients);
        setSteps(steps);
    }

    public RecetteDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
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
}

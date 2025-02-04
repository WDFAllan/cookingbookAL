package com.example.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Recette {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Double rate;
    private LocalDate date;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients;

    @ElementCollection
    private List<String> steps;

    @ElementCollection
    private List<String> tags = new ArrayList<>();

    public Recette(Integer id, String name, Double rate, List<String> tags, List<Ingredient> ingredients, List<String> steps) {
        setId(id);
        setName(name);
        setRate(rate);
        setDate(LocalDate.now());
        setTags(tags);
        setIngredients(ingredients);
        setSteps(steps);
    }

    public Recette(String name, Double rate, List<String> tags, List<String> steps, List<Ingredient> ingredients) {
        setName(name);
        setRate(rate);
        setDate(LocalDate.now());
        setTags(tags);
        setSteps(steps);
        setIngredients(ingredients);
    }

    public Recette() {}

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

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Recette{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rate=" + rate +
                ", date=" + date +
                ", tags=" + tags +
                '}';
    }

}

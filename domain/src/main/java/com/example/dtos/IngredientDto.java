package com.example.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class IngredientDto {

    private Integer id;

    @NotBlank(message = "Le nom de l'ingrédient est obligatoire")
    private String name;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être supérieure à 0")
    private Integer quantity;

    @NotBlank(message = "L'unité est obligatoire")
    private String unit;

    public IngredientDto(Integer id, String name, Integer quantity, String unit) {
        setId(id);
        setName(name);
        setQuantity(quantity);
        setUnit(unit);
    }

    public IngredientDto(String name, Integer quantity, String unit) {
        setName(name);
        setQuantity(quantity);
        setUnit(unit);
    }

    public IngredientDto() {}

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

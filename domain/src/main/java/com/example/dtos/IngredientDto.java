package com.example.dtos;

public class IngredientDto {

    private Integer id;
    private String name;
    private Integer quantity;
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

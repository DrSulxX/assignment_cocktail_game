package com.ridango.game.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TEMP")
public class Cocktail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "cocktail_id", nullable = false, length = 255)
    private String idDrink;

    @Column(name = "TEMP", nullable = false, length = 255)
    private String strDrink;

    @Column(name = "instructions", length = 3000) // Increased to 3000 characters
    private String strInstructions;

    @Column(name = "category", length = 255)
    private String strCategory;

    @Column(name = "glass", length = 255)
    private String strGlass;

    @Column(name = "image_url", length = 1000) // Increased to 1000 characters
    private String strDrinkThumb;

    // Separate columns for each ingredient, with increased length
    @Column(name = "ingredient1", length = 255)
    private String strIngredient1;

    @Column(name = "ingredient2", length = 255)
    private String strIngredient2;

    @Column(name = "ingredient3", length = 255)
    private String strIngredient3;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdDrink() {
        return idDrink;
    }

    public void setIdDrink(String idDrink) {
        this.idDrink = idDrink;
    }

    public String getStrDrink() {
        return strDrink;
    }

    public void setStrDrink(String strDrink) {
        this.strDrink = strDrink;
    }

    public String getStrInstructions() {
        return strInstructions;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public String getStrGlass() {
        return strGlass;
    }

    public void setStrGlass(String strGlass) {
        this.strGlass = strGlass;
    }

    public String getStrDrinkThumb() {
        return strDrinkThumb;
    }

    public void setStrDrinkThumb(String strDrinkThumb) {
        this.strDrinkThumb = strDrinkThumb;
    }

    public String getStrIngredient1() {
        return strIngredient1;
    }

    public void setStrIngredient1(String strIngredient1) {
        this.strIngredient1 = strIngredient1;
    }

    public String getStrIngredient2() {
        return strIngredient2;
    }

    public void setStrIngredient2(String strIngredient2) {
        this.strIngredient2 = strIngredient2;
    }

    public String getStrIngredient3() {
        return strIngredient3;
    }

    public void setStrIngredient3(String strIngredient3) {
        this.strIngredient3 = strIngredient3;
    }
}
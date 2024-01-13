package com.chefapp.Models;

public class FoodItem {
    private String foodItemId;

    private String name;
    private String ingredients;
    private String costPrice;
    private String sellingPrice;
    private String timeToCook;
    private String category;
    private String subcategory;
    private String imageUrl;
    private int status;
    public FoodItem() {
        // Default constructor required for calls to DataSnapshot.getValue(FoodItem.class)
    }


    public FoodItem(String name, String ingredients, String costPrice, String sellingPrice, String timeToCook, String category, String subcategory, String imageUrl, int status) {
        this.name = name;
        this.ingredients = ingredients;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.timeToCook = timeToCook;
        this.category = category;
        this.subcategory = subcategory;
        this.imageUrl = imageUrl;
        this.status = status;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getTimeToCook() {
        return timeToCook;
    }

    public void setTimeToCook(String timeToCook) {
        this.timeToCook = timeToCook;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(String foodItemId) {
        this.foodItemId = foodItemId;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

//
//    public String getChefname() {
//        return chefname;
//    }
//
//    public void setChefname(String chefname) {
//        this.chefname = chefname;
//    }
}

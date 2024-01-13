package com.chefapp.Models;

import com.google.firebase.database.PropertyName;

public class Order {
    private String orderId;
    private String category;
    private String costPrice;
    private String ingredients;
    private String name;
    private String sellingPrice;
    private String subcategory;
    private String timeToCook;
    private int status;
    @PropertyName("username")
    private String username;

    private String statusDenied;
    private String completedTime;
//    private String orderTime;
//    private String foodid;
private String chefUsername;
    private String deniedChefUsername;
    private String imageUrl;



//    private String completionTime;

//    private String formattedCompletionTime;



    public Order() {
        // Default constructor required for Firebase
    }

    public Order(String orderId, String category, String costPrice, String ingredients,
                 String name, String sellingPrice, String subcategory, String timeToCook)
    {
        this.orderId = orderId;
        this.category = category;
        this.costPrice = costPrice;
        this.ingredients = ingredients;
        this.name = name;
        this.sellingPrice = sellingPrice;
        this.subcategory = subcategory;
        this.timeToCook = timeToCook;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getTimeToCook() {
        return timeToCook;
    }

    public void setTimeToCook(String timeToCook) {
        this.timeToCook = timeToCook;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDenied() {
        return statusDenied;
    }

    public void setStatusDenied(String statusDenied) {
        this.statusDenied = statusDenied;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(String completedTime) {
        this.completedTime = completedTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChefUsername() {
        return chefUsername;
    }

    public void setChefUsername(String chefUsername) {
        this.chefUsername = chefUsername;
    }

    public String getDeniedChefUsername() {
        return deniedChefUsername;
    }

    public void setDeniedChefUsername(String deniedChefUsername) {
        this.deniedChefUsername = deniedChefUsername;
    }
    //    public String getOrderTime() {
//        return orderTime;
//    }
//
//    public void setOrderTime(String orderTime) {
//        this.orderTime = orderTime;
//    }

//    public String getFoodid() {
//        return foodid;
//    }
//
//    public void setFoodid(String foodid) {
//        this.foodid = foodid;
//    }
}

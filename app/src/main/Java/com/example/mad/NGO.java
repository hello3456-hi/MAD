package com.example.mad;

public class NGO {
    private String name;
    private String location;
    private String description;
    private double rating;
    private int campaigns;
    private int meals;
    private boolean verified;

    public NGO(String name, String location, String description, double rating, int campaigns, int meals, boolean verified) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.rating = rating;
        this.campaigns = campaigns;
        this.meals = meals;
        this.verified = verified;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }

    public int getCampaigns() {
        return campaigns;
    }

    public int getMeals() {
        return meals;
    }

    public boolean isVerified() {
        return verified;
    }
}

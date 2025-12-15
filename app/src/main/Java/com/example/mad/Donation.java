package com.example.mad;

public class Donation {
    private String campaignName;
    private String date;
    private int amount;
    private int meals;

    public Donation(String campaignName, String date, int amount, int meals) {
        this.campaignName = campaignName;
        this.date = date;
        this.amount = amount;
        this.meals = meals;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public String getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public int getMeals() {
        return meals;
    }
}
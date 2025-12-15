package com.example.mad;

import java.util.ArrayList;
import java.util.List;

public class DonationData {
    // SharedPreferences Name
    public static final String donation_data = "donation_data";

    // Keys for saving raised amounts
    public static final String sudan_raised = "sudan_raised";
    public static final String ukraine_raised = "ukraine_raised";
    public static final String india_raised = "india_raised";
    public static final String vietnam_raised = "vietnam_raised";

    // Goal constants
    public static final int sudan_goal = 20000;
    public static final int ukraine_goal = 15000;
    public static final int india_goal = 10000;
    public static final int vietnam_goal = 25000;

    // --- THIS WAS MISSING ---
    // A static list to store real donations while the app is running
    public static List<Donation> historyList = new ArrayList<>();
}
package com.example.spendly;

/**
 * Simple POJO representing a gamification badge.
 * Badges are computed on the fly from existing Expense / BudgetGoal data,
 * so no extra database table is required.
 */
public class Achievement {
    public String emoji;
    public String title;
    public String description;
    public boolean unlocked;

    public Achievement(String emoji, String title, String description, boolean unlocked) {
        this.emoji = emoji;
        this.title = title;
        this.description = description;
        this.unlocked = unlocked;
    }
}

package com.example.spendly;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import entity.AppDatabase;
import entity.BudgetGoal;
import entity.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

/**
 * Gamification screen: shows the user's daily logging streak and a list of
 * badges that unlock based on consistent expense logging and meeting
 * budget goals. Badges are computed dynamically from existing Expense and
 * BudgetGoal data - no extra database table required.
 */
public class Achievements extends AppCompatActivity {

    private TextView tvStreakCount;
    private RecyclerView rvAchievements;
    private AchievementAdapter adapter;
    private AppDatabase db;

    private List<Expense> allExpenses = new ArrayList<>();
    private BudgetGoal budgetGoal;

    private final SimpleDateFormat dayKeyFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        setTitle("Achievements");

        db = AppDatabase.getDatabase(this);

        tvStreakCount = findViewById(R.id.tvStreakCount);
        rvAchievements = findViewById(R.id.rvAchievements);
        rvAchievements.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AchievementAdapter();
        rvAchievements.setAdapter(adapter);

        db.expenseDao().getAllExpenses().observe(this, expenses -> {
            allExpenses = expenses != null ? expenses : new ArrayList<>();
            refresh();
        });

        db.budgetGoalDao().getBudgetGoal().observe(this, goal -> {
            budgetGoal = goal;
            refresh();
        });
    }

    private void refresh() {
        int streak = calculateStreak(allExpenses);
        int totalDaysLogged = distinctDays(allExpenses).size();

        tvStreakCount.setText(streak + (streak == 1 ? " Day Streak" : " Day Streak"));

        double lastMonthSpend = lastMonthSpend(allExpenses);
        double maxGoal = budgetGoal != null ? budgetGoal.maxMonthlyGoal : 0;
        double minGoal = budgetGoal != null ? budgetGoal.minMonthlyGoal : 0;

        List<Achievement> achievements = new ArrayList<>();

        achievements.add(new Achievement("🌱", "First Steps",
                "Log your very first expense",
                !allExpenses.isEmpty()));

        achievements.add(new Achievement("🔥", "3-Day Streak",
                "Log an expense for 3 days in a row",
                streak >= 3));

        achievements.add(new Achievement("🔥🔥", "7-Day Streak",
                "Log an expense for 7 days in a row",
                streak >= 7));

        achievements.add(new Achievement("📝", "Habit Builder",
                "Log expenses on 10 different days",
                totalDaysLogged >= 10));

        achievements.add(new Achievement("🎯", "Goal Setter",
                "Set a minimum or maximum monthly budget goal",
                (maxGoal > 0 || minGoal > 0)));

        achievements.add(new Achievement("💪", "Budget Master",
                "Spend within your maximum goal last month",
                maxGoal > 0 && lastMonthSpend > 0 && lastMonthSpend <= maxGoal));

        achievements.add(new Achievement("💰", "On Target",
                "Land between your min and max goals last month",
                minGoal > 0 && maxGoal > 0 && lastMonthSpend >= minGoal && lastMonthSpend <= maxGoal));

        adapter.setAchievements(achievements);
    }

    /** Returns the set of distinct calendar days (yyyy-MM-dd) that have at least one expense. */
    private Set<String> distinctDays(List<Expense> expenses) {
        Set<String> days = new TreeSet<>();
        for (Expense e : expenses) {
            days.add(dayKeyFormat.format(new java.util.Date(e.date)));
        }
        return days;
    }

    /**
     * Calculates the current consecutive-day logging streak, counting backwards
     * from today (or yesterday, so the streak isn't lost before today's entry is made).
     */
    private int calculateStreak(List<Expense> expenses) {
        Set<String> days = new HashSet<>();
        for (Expense e : expenses) {
            days.add(dayKeyFormat.format(new java.util.Date(e.date)));
        }
        if (days.isEmpty()) return 0;

        Calendar cal = Calendar.getInstance();
        // If nothing logged today, start counting from yesterday so the streak
        // isn't shown as broken before the user has had a chance to log today.
        if (!days.contains(dayKeyFormat.format(cal.getTime()))) {
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }

        int streak = 0;
        while (days.contains(dayKeyFormat.format(cal.getTime()))) {
            streak++;
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }
        return streak;
    }

    /** Total amount spent during the previous full calendar month. */
    private double lastMonthSpend(List<Expense> expenses) {
        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        start.set(Calendar.DAY_OF_MONTH, 1);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.DAY_OF_MONTH, 1);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);

        long startMillis = start.getTimeInMillis();
        long endMillis = end.getTimeInMillis();

        double total = 0;
        for (Expense e : expenses) {
            if (e.date >= startMillis && e.date < endMillis) {
                total += e.amount;
            }
        }
        return total;
    }
}

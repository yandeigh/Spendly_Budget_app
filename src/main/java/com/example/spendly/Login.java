package com.example.spendly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import entity.AppDatabase;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Login extends AppCompatActivity {
    private TextView tvWelcome, tvBudgetText, tvGoalRange, tvStreakBadge;
    private ProgressBar pbMonthlyBudget;
    private Button btnAddExpense, btnCategories, btnReports, btnGoals, btnLogout, btnAchievements;
    private RecyclerView rvRecentExpenses;
    private RecentExpensesAdapter recentAdapter;
    private AppDatabase db;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "ZA"));
    private final SimpleDateFormat dayKeyFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private double currentSpent = 0;
    private entity.BudgetGoal currentGoal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // <-- updated layout name

        db = AppDatabase.getDatabase(this);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvBudgetText = findViewById(R.id.tvBudgetText);
        tvGoalRange = findViewById(R.id.tvGoalRange);
        tvStreakBadge = findViewById(R.id.tvStreakBadge);
        pbMonthlyBudget = findViewById(R.id.pbMonthlyBudget);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnCategories = findViewById(R.id.btnCategories);
        btnReports = findViewById(R.id.btnReports);
        btnGoals = findViewById(R.id.btnGoals);
        btnAchievements = findViewById(R.id.btnAchievements);

        rvRecentExpenses = findViewById(R.id.rvRecentExpenses);
        rvRecentExpenses.setLayoutManager(new LinearLayoutManager(this));
        recentAdapter = new RecentExpensesAdapter(currencyFormat);
        rvRecentExpenses.setAdapter(recentAdapter);

        String username = getIntent().getStringExtra("USERNAME");
        if (username == null) {
            SharedPreferences prefs = getSharedPreferences("SpendlyPrefs", MODE_PRIVATE);
            username = prefs.getString("USERNAME", "User");
        }
        tvWelcome.setText("Hello, " + username + " 👋");

        setupNavigation();
        loadDashboardData();
        loadRecentExpenses();
        loadStreak();
    }

    private void setupNavigation() {
        btnAddExpense.setOnClickListener(v -> startActivity(new Intent(this, AddExpenses.class)));
        btnCategories.setOnClickListener(v -> startActivity(new Intent(this, Category.class)));
        btnReports.setOnClickListener(v -> startActivity(new Intent(this, Reports.class)));
        btnGoals.setOnClickListener(v -> startActivity(new Intent(this, BudgetGoal.class)));

        btnAchievements.setOnClickListener(v -> startActivity(new Intent(this, Achievements.class)));

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Logout", (d, w) -> {
                        getSharedPreferences("SpendlyPrefs", MODE_PRIVATE)
                                .edit().remove("USERNAME").apply();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardData();
        loadRecentExpenses();
        loadStreak();
    }

    private void loadDashboardData() {
        Calendar startOfMonth = Calendar.getInstance();
        startOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        startOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        startOfMonth.set(Calendar.MINUTE, 0);
        startOfMonth.set(Calendar.SECOND, 0);

        Calendar endOfMonth = Calendar.getInstance();
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
        endOfMonth.set(Calendar.HOUR_OF_DAY, 23);
        endOfMonth.set(Calendar.MINUTE, 59);

        db.expenseDao().getExpensesInRange(startOfMonth.getTimeInMillis(), endOfMonth.getTimeInMillis())
                .observe(this, expenses -> {
                    currentSpent = 0;
                    if (expenses != null) for (entity.Expense e : expenses) currentSpent += e.amount;
                    updateBudgetUI();
                });

        db.budgetGoalDao().getBudgetGoal()
                .observe(this, goal -> {
                    currentGoal = goal;
                    updateBudgetUI();
                });
    }

    private void updateBudgetUI() {
        double maxGoal = (currentGoal != null && currentGoal.maxMonthlyGoal > 0)
                ? currentGoal.maxMonthlyGoal : 0;
        double minGoal = (currentGoal != null && currentGoal.minMonthlyGoal > 0)
                ? currentGoal.minMonthlyGoal : 0;

        tvBudgetText.setText(currencyFormat.format(currentSpent) + " / " +
                (maxGoal > 0 ? currencyFormat.format(maxGoal) : "No goal set"));

        if (maxGoal > 0) {
            int progress = (int) Math.min(100, (currentSpent / maxGoal) * 100);
            pbMonthlyBudget.setProgress(progress);

            // Overspending categories / overall budget are highlighted visually.
            if (currentSpent > maxGoal) {
                pbMonthlyBudget.setProgressTintList(android.content.res.ColorStateList.valueOf(0xFFE53935)); // red
                tvBudgetText.setTextColor(0xFFE53935);
            } else if (minGoal > 0 && currentSpent < minGoal) {
                pbMonthlyBudget.setProgressTintList(android.content.res.ColorStateList.valueOf(0xFFFFC107)); // amber
                tvBudgetText.setTextColor(0xFF888888);
            } else {
                pbMonthlyBudget.setProgressTintList(android.content.res.ColorStateList.valueOf(0xFF4CAF50)); // green
                tvBudgetText.setTextColor(0xFF888888);
            }
        } else {
            pbMonthlyBudget.setProgress(0);
            pbMonthlyBudget.setProgressTintList(android.content.res.ColorStateList.valueOf(0xFFFFFFFF));
            tvBudgetText.setTextColor(0xFF888888);
        }

        if (minGoal > 0 || maxGoal > 0) {
            tvGoalRange.setText("Goal range: " +
                    (minGoal > 0 ? currencyFormat.format(minGoal) : "—") + " – " +
                    (maxGoal > 0 ? currencyFormat.format(maxGoal) : "—"));
        } else {
            tvGoalRange.setText("");
        }
    }

    /** Calculates and displays the user's current consecutive-day expense logging streak. */
    private void loadStreak() {
        db.expenseDao().getAllExpenses().observe(this, expenses -> {
            Set<String> days = new HashSet<>();
            if (expenses != null) {
                for (entity.Expense e : expenses) {
                    days.add(dayKeyFormat.format(new java.util.Date(e.date)));
                }
            }

            int streak = 0;
            if (!days.isEmpty()) {
                Calendar cal = Calendar.getInstance();
                if (!days.contains(dayKeyFormat.format(cal.getTime()))) {
                    cal.add(Calendar.DAY_OF_YEAR, -1);
                }
                while (days.contains(dayKeyFormat.format(cal.getTime()))) {
                    streak++;
                    cal.add(Calendar.DAY_OF_YEAR, -1);
                }
            }

            tvStreakBadge.setText("🔥 " + streak + (streak == 1 ? " Day Streak" : " Day Streak"));
        });
    }

    private void loadRecentExpenses() {
        db.expenseDao().getRecentExpenses(5).observe(this, expenses -> {
            recentAdapter.setExpenses(expenses);
        });
    }
}
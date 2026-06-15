package com.example.spendly;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import entity.AppDatabase;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.Executors;

public class BudgetGoal extends AppCompatActivity {
    private SeekBar sbMinGoal, sbMaxGoal;
    private TextView tvMinGoalLabel, tvMaxGoalLabel;
    private Button btnSaveGoals;
    private AppDatabase db;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "ZA"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_goal);

        db = AppDatabase.getDatabase(this);
        sbMinGoal = findViewById(R.id.sbMinGoal);
        sbMaxGoal = findViewById(R.id.sbMaxGoal);
        tvMinGoalLabel = findViewById(R.id.tvMinGoalLabel);
        tvMaxGoalLabel = findViewById(R.id.tvMaxGoalLabel);
        btnSaveGoals = findViewById(R.id.btnSaveGoals);

        // Load existing goal and pre-fill seekbars
        db.budgetGoalDao().getBudgetGoal().observe(this, goal -> {
            if (goal != null) {
                sbMinGoal.setProgress((int) goal.minMonthlyGoal);
                sbMaxGoal.setProgress((int) goal.maxMonthlyGoal);
                tvMinGoalLabel.setText("Minimum Spending Goal: " + currencyFormat.format(goal.minMonthlyGoal));
                tvMaxGoalLabel.setText("Maximum Spending Goal: " + currencyFormat.format(goal.maxMonthlyGoal));
            }
        });

        sbMinGoal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvMinGoalLabel.setText("Minimum Spending Goal: " + currencyFormat.format(progress));
                // Enforce min <= max
                if (progress > sbMaxGoal.getProgress()) {
                    sbMaxGoal.setProgress(progress);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        sbMaxGoal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvMaxGoalLabel.setText("Maximum Spending Goal: " + currencyFormat.format(progress));
                // Enforce max >= min
                if (progress < sbMinGoal.getProgress()) {
                    sbMinGoal.setProgress(progress);
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSaveGoals.setOnClickListener(v -> {
            entity.BudgetGoal goal = new entity.BudgetGoal();
            goal.minMonthlyGoal = sbMinGoal.getProgress();
            goal.maxMonthlyGoal = sbMaxGoal.getProgress();

            if (goal.minMonthlyGoal == 0 && goal.maxMonthlyGoal == 0) {
                Toast.makeText(this, "Please set at least one goal", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                db.budgetGoalDao().insertOrUpdate(goal);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Goals saved!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }
}

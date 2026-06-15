package entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "budget_goals")
public class BudgetGoal {
    @PrimaryKey
    public int id = 1; // Single record for global goals
    public double minMonthlyGoal;
    public double maxMonthlyGoal;
}

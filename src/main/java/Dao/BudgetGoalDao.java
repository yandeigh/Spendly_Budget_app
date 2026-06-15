package Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import entity.BudgetGoal;

@Dao
public interface BudgetGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(BudgetGoal goal);

    @Query("SELECT * FROM budget_goals WHERE id = 1")
    LiveData<BudgetGoal> getBudgetGoal();
}

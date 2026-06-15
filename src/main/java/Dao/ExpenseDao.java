package Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import entity.Expense;
import java.util.List;

@Dao
public interface ExpenseDao {

    @Insert
    void insert(Expense expense);

    @Update
    void update(Expense expense);

    @Delete
    void delete(Expense expense);

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    LiveData<List<Expense>> getAllExpenses();

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    List<Expense> getAllExpensesSync();

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<Expense>> getExpensesInRange(long startDate, long endDate);

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    List<Expense> getExpensesInRangeSync(long startDate, long endDate);

    @Query("SELECT categoryId, SUM(amount) as total FROM expenses WHERE date BETWEEN :startDate AND :endDate GROUP BY categoryId")
    LiveData<List<CategoryTotal>> getCategoryTotalsInRange(long startDate, long endDate);

    @Query("SELECT categoryId, SUM(amount) as total FROM expenses WHERE date BETWEEN :startDate AND :endDate GROUP BY categoryId")
    List<CategoryTotal> getCategoryTotalsInRangeSync(long startDate, long endDate);

    @Query("SELECT * FROM expenses ORDER BY date DESC LIMIT :limit")
    LiveData<List<Expense>> getRecentExpenses(int limit);

    class CategoryTotal {
        public int categoryId;
        public double total;
    }
}
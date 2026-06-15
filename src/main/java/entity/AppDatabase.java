package entity;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import Dao.CategoryDao;
import Dao.ExpenseDao;
import Dao.UserDao;
import Dao.BudgetGoalDao;
import entity.Category;
import entity.Expense;
import entity.User;
import entity.BudgetGoal;

@Database(entities = {User.class, Category.class, Expense.class, BudgetGoal.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract CategoryDao categoryDao();
    public abstract ExpenseDao expenseDao();
    public abstract BudgetGoalDao budgetGoalDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "spendly_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}


package Dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import entity.BudgetGoal;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class BudgetGoalDao_Impl implements BudgetGoalDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<BudgetGoal> __insertAdapterOfBudgetGoal;

  public BudgetGoalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfBudgetGoal = new EntityInsertAdapter<BudgetGoal>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `budget_goals` (`id`,`minMonthlyGoal`,`maxMonthlyGoal`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final BudgetGoal entity) {
        statement.bindLong(1, entity.id);
        statement.bindDouble(2, entity.minMonthlyGoal);
        statement.bindDouble(3, entity.maxMonthlyGoal);
      }
    };
  }

  @Override
  public void insertOrUpdate(final BudgetGoal goal) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfBudgetGoal.insert(_connection, goal);
      return null;
    });
  }

  @Override
  public LiveData<BudgetGoal> getBudgetGoal() {
    final String _sql = "SELECT * FROM budget_goals WHERE id = 1";
    return __db.getInvalidationTracker().createLiveData(new String[] {"budget_goals"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfMinMonthlyGoal = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "minMonthlyGoal");
        final int _columnIndexOfMaxMonthlyGoal = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "maxMonthlyGoal");
        final BudgetGoal _result;
        if (_stmt.step()) {
          _result = new BudgetGoal();
          _result.id = (int) (_stmt.getLong(_columnIndexOfId));
          _result.minMonthlyGoal = _stmt.getDouble(_columnIndexOfMinMonthlyGoal);
          _result.maxMonthlyGoal = _stmt.getDouble(_columnIndexOfMaxMonthlyGoal);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

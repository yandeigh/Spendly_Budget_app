package Dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import entity.Expense;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class ExpenseDao_Impl implements ExpenseDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Expense> __insertAdapterOfExpense;

  private final EntityDeleteOrUpdateAdapter<Expense> __deleteAdapterOfExpense;

  private final EntityDeleteOrUpdateAdapter<Expense> __updateAdapterOfExpense;

  public ExpenseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfExpense = new EntityInsertAdapter<Expense>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `expenses` (`id`,`amount`,`date`,`description`,`categoryId`,`photoPath`,`startTime`,`endTime`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Expense entity) {
        statement.bindLong(1, entity.id);
        statement.bindDouble(2, entity.amount);
        statement.bindLong(3, entity.date);
        if (entity.description == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.description);
        }
        statement.bindLong(5, entity.categoryId);
        if (entity.photoPath == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.photoPath);
        }
        if (entity.startTime == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.startTime);
        }
        if (entity.endTime == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.endTime);
        }
      }
    };
    this.__deleteAdapterOfExpense = new EntityDeleteOrUpdateAdapter<Expense>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `expenses` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Expense entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfExpense = new EntityDeleteOrUpdateAdapter<Expense>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `expenses` SET `id` = ?,`amount` = ?,`date` = ?,`description` = ?,`categoryId` = ?,`photoPath` = ?,`startTime` = ?,`endTime` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Expense entity) {
        statement.bindLong(1, entity.id);
        statement.bindDouble(2, entity.amount);
        statement.bindLong(3, entity.date);
        if (entity.description == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.description);
        }
        statement.bindLong(5, entity.categoryId);
        if (entity.photoPath == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.photoPath);
        }
        if (entity.startTime == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.startTime);
        }
        if (entity.endTime == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.endTime);
        }
        statement.bindLong(9, entity.id);
      }
    };
  }

  @Override
  public void insert(final Expense expense) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfExpense.insert(_connection, expense);
      return null;
    });
  }

  @Override
  public void delete(final Expense expense) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfExpense.handle(_connection, expense);
      return null;
    });
  }

  @Override
  public void update(final Expense expense) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfExpense.handle(_connection, expense);
      return null;
    });
  }

  @Override
  public LiveData<List<Expense>> getAllExpenses() {
    final String _sql = "SELECT * FROM expenses ORDER BY date DESC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"expenses"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfAmount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "amount");
        final int _columnIndexOfDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "date");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfCategoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categoryId");
        final int _columnIndexOfPhotoPath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "photoPath");
        final int _columnIndexOfStartTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startTime");
        final int _columnIndexOfEndTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endTime");
        final List<Expense> _result = new ArrayList<Expense>();
        while (_stmt.step()) {
          final Expense _item;
          _item = new Expense();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          _item.amount = _stmt.getDouble(_columnIndexOfAmount);
          _item.date = _stmt.getLong(_columnIndexOfDate);
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _item.description = null;
          } else {
            _item.description = _stmt.getText(_columnIndexOfDescription);
          }
          _item.categoryId = (int) (_stmt.getLong(_columnIndexOfCategoryId));
          if (_stmt.isNull(_columnIndexOfPhotoPath)) {
            _item.photoPath = null;
          } else {
            _item.photoPath = _stmt.getText(_columnIndexOfPhotoPath);
          }
          if (_stmt.isNull(_columnIndexOfStartTime)) {
            _item.startTime = null;
          } else {
            _item.startTime = _stmt.getText(_columnIndexOfStartTime);
          }
          if (_stmt.isNull(_columnIndexOfEndTime)) {
            _item.endTime = null;
          } else {
            _item.endTime = _stmt.getText(_columnIndexOfEndTime);
          }
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Expense> getAllExpensesSync() {
    final String _sql = "SELECT * FROM expenses ORDER BY date DESC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfAmount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "amount");
        final int _columnIndexOfDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "date");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfCategoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categoryId");
        final int _columnIndexOfPhotoPath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "photoPath");
        final int _columnIndexOfStartTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startTime");
        final int _columnIndexOfEndTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endTime");
        final List<Expense> _result = new ArrayList<Expense>();
        while (_stmt.step()) {
          final Expense _item;
          _item = new Expense();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          _item.amount = _stmt.getDouble(_columnIndexOfAmount);
          _item.date = _stmt.getLong(_columnIndexOfDate);
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _item.description = null;
          } else {
            _item.description = _stmt.getText(_columnIndexOfDescription);
          }
          _item.categoryId = (int) (_stmt.getLong(_columnIndexOfCategoryId));
          if (_stmt.isNull(_columnIndexOfPhotoPath)) {
            _item.photoPath = null;
          } else {
            _item.photoPath = _stmt.getText(_columnIndexOfPhotoPath);
          }
          if (_stmt.isNull(_columnIndexOfStartTime)) {
            _item.startTime = null;
          } else {
            _item.startTime = _stmt.getText(_columnIndexOfStartTime);
          }
          if (_stmt.isNull(_columnIndexOfEndTime)) {
            _item.endTime = null;
          } else {
            _item.endTime = _stmt.getText(_columnIndexOfEndTime);
          }
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<List<Expense>> getExpensesInRange(final long startDate, final long endDate) {
    final String _sql = "SELECT * FROM expenses WHERE date BETWEEN ? AND ? ORDER BY date DESC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"expenses"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, startDate);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, endDate);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfAmount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "amount");
        final int _columnIndexOfDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "date");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfCategoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categoryId");
        final int _columnIndexOfPhotoPath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "photoPath");
        final int _columnIndexOfStartTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startTime");
        final int _columnIndexOfEndTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endTime");
        final List<Expense> _result = new ArrayList<Expense>();
        while (_stmt.step()) {
          final Expense _item;
          _item = new Expense();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          _item.amount = _stmt.getDouble(_columnIndexOfAmount);
          _item.date = _stmt.getLong(_columnIndexOfDate);
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _item.description = null;
          } else {
            _item.description = _stmt.getText(_columnIndexOfDescription);
          }
          _item.categoryId = (int) (_stmt.getLong(_columnIndexOfCategoryId));
          if (_stmt.isNull(_columnIndexOfPhotoPath)) {
            _item.photoPath = null;
          } else {
            _item.photoPath = _stmt.getText(_columnIndexOfPhotoPath);
          }
          if (_stmt.isNull(_columnIndexOfStartTime)) {
            _item.startTime = null;
          } else {
            _item.startTime = _stmt.getText(_columnIndexOfStartTime);
          }
          if (_stmt.isNull(_columnIndexOfEndTime)) {
            _item.endTime = null;
          } else {
            _item.endTime = _stmt.getText(_columnIndexOfEndTime);
          }
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<Expense> getExpensesInRangeSync(final long startDate, final long endDate) {
    final String _sql = "SELECT * FROM expenses WHERE date BETWEEN ? AND ? ORDER BY date DESC";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, startDate);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, endDate);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfAmount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "amount");
        final int _columnIndexOfDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "date");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfCategoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categoryId");
        final int _columnIndexOfPhotoPath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "photoPath");
        final int _columnIndexOfStartTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startTime");
        final int _columnIndexOfEndTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endTime");
        final List<Expense> _result = new ArrayList<Expense>();
        while (_stmt.step()) {
          final Expense _item;
          _item = new Expense();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          _item.amount = _stmt.getDouble(_columnIndexOfAmount);
          _item.date = _stmt.getLong(_columnIndexOfDate);
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _item.description = null;
          } else {
            _item.description = _stmt.getText(_columnIndexOfDescription);
          }
          _item.categoryId = (int) (_stmt.getLong(_columnIndexOfCategoryId));
          if (_stmt.isNull(_columnIndexOfPhotoPath)) {
            _item.photoPath = null;
          } else {
            _item.photoPath = _stmt.getText(_columnIndexOfPhotoPath);
          }
          if (_stmt.isNull(_columnIndexOfStartTime)) {
            _item.startTime = null;
          } else {
            _item.startTime = _stmt.getText(_columnIndexOfStartTime);
          }
          if (_stmt.isNull(_columnIndexOfEndTime)) {
            _item.endTime = null;
          } else {
            _item.endTime = _stmt.getText(_columnIndexOfEndTime);
          }
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<List<ExpenseDao.CategoryTotal>> getCategoryTotalsInRange(final long startDate,
      final long endDate) {
    final String _sql = "SELECT categoryId, SUM(amount) as total FROM expenses WHERE date BETWEEN ? AND ? GROUP BY categoryId";
    return __db.getInvalidationTracker().createLiveData(new String[] {"expenses"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, startDate);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, endDate);
        final int _columnIndexOfCategoryId = 0;
        final int _columnIndexOfTotal = 1;
        final List<ExpenseDao.CategoryTotal> _result = new ArrayList<ExpenseDao.CategoryTotal>();
        while (_stmt.step()) {
          final ExpenseDao.CategoryTotal _item;
          _item = new ExpenseDao.CategoryTotal();
          _item.categoryId = (int) (_stmt.getLong(_columnIndexOfCategoryId));
          _item.total = _stmt.getDouble(_columnIndexOfTotal);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public List<ExpenseDao.CategoryTotal> getCategoryTotalsInRangeSync(final long startDate,
      final long endDate) {
    final String _sql = "SELECT categoryId, SUM(amount) as total FROM expenses WHERE date BETWEEN ? AND ? GROUP BY categoryId";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, startDate);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, endDate);
        final int _columnIndexOfCategoryId = 0;
        final int _columnIndexOfTotal = 1;
        final List<ExpenseDao.CategoryTotal> _result = new ArrayList<ExpenseDao.CategoryTotal>();
        while (_stmt.step()) {
          final ExpenseDao.CategoryTotal _item;
          _item = new ExpenseDao.CategoryTotal();
          _item.categoryId = (int) (_stmt.getLong(_columnIndexOfCategoryId));
          _item.total = _stmt.getDouble(_columnIndexOfTotal);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<List<Expense>> getRecentExpenses(final int limit) {
    final String _sql = "SELECT * FROM expenses ORDER BY date DESC LIMIT ?";
    return __db.getInvalidationTracker().createLiveData(new String[] {"expenses"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, limit);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfAmount = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "amount");
        final int _columnIndexOfDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "date");
        final int _columnIndexOfDescription = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "description");
        final int _columnIndexOfCategoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "categoryId");
        final int _columnIndexOfPhotoPath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "photoPath");
        final int _columnIndexOfStartTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startTime");
        final int _columnIndexOfEndTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endTime");
        final List<Expense> _result = new ArrayList<Expense>();
        while (_stmt.step()) {
          final Expense _item;
          _item = new Expense();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          _item.amount = _stmt.getDouble(_columnIndexOfAmount);
          _item.date = _stmt.getLong(_columnIndexOfDate);
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _item.description = null;
          } else {
            _item.description = _stmt.getText(_columnIndexOfDescription);
          }
          _item.categoryId = (int) (_stmt.getLong(_columnIndexOfCategoryId));
          if (_stmt.isNull(_columnIndexOfPhotoPath)) {
            _item.photoPath = null;
          } else {
            _item.photoPath = _stmt.getText(_columnIndexOfPhotoPath);
          }
          if (_stmt.isNull(_columnIndexOfStartTime)) {
            _item.startTime = null;
          } else {
            _item.startTime = _stmt.getText(_columnIndexOfStartTime);
          }
          if (_stmt.isNull(_columnIndexOfEndTime)) {
            _item.endTime = null;
          } else {
            _item.endTime = _stmt.getText(_columnIndexOfEndTime);
          }
          _result.add(_item);
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

package Dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import entity.Category;
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
public final class CategoryDao_Impl implements CategoryDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Category> __insertAdapterOfCategory;

  private final EntityDeleteOrUpdateAdapter<Category> __deleteAdapterOfCategory;

  private final EntityDeleteOrUpdateAdapter<Category> __updateAdapterOfCategory;

  public CategoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfCategory = new EntityInsertAdapter<Category>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `categories` (`id`,`name`,`icon`,`color`,`budgetLimit`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Category entity) {
        statement.bindLong(1, entity.id);
        if (entity.name == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.name);
        }
        if (entity.icon == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.icon);
        }
        if (entity.color == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.color);
        }
        statement.bindDouble(5, entity.budgetLimit);
      }
    };
    this.__deleteAdapterOfCategory = new EntityDeleteOrUpdateAdapter<Category>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `categories` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Category entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfCategory = new EntityDeleteOrUpdateAdapter<Category>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `categories` SET `id` = ?,`name` = ?,`icon` = ?,`color` = ?,`budgetLimit` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Category entity) {
        statement.bindLong(1, entity.id);
        if (entity.name == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.name);
        }
        if (entity.icon == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.icon);
        }
        if (entity.color == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.color);
        }
        statement.bindDouble(5, entity.budgetLimit);
        statement.bindLong(6, entity.id);
      }
    };
  }

  @Override
  public void insert(final Category category) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfCategory.insert(_connection, category);
      return null;
    });
  }

  @Override
  public void delete(final Category category) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __deleteAdapterOfCategory.handle(_connection, category);
      return null;
    });
  }

  @Override
  public void update(final Category category) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __updateAdapterOfCategory.handle(_connection, category);
      return null;
    });
  }

  @Override
  public LiveData<List<Category>> getAllCategories() {
    final String _sql = "SELECT * FROM categories";
    return __db.getInvalidationTracker().createLiveData(new String[] {"categories"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfIcon = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "icon");
        final int _columnIndexOfColor = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "color");
        final int _columnIndexOfBudgetLimit = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "budgetLimit");
        final List<Category> _result = new ArrayList<Category>();
        while (_stmt.step()) {
          final Category _item;
          _item = new Category();
          _item.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfName)) {
            _item.name = null;
          } else {
            _item.name = _stmt.getText(_columnIndexOfName);
          }
          if (_stmt.isNull(_columnIndexOfIcon)) {
            _item.icon = null;
          } else {
            _item.icon = _stmt.getText(_columnIndexOfIcon);
          }
          if (_stmt.isNull(_columnIndexOfColor)) {
            _item.color = null;
          } else {
            _item.color = _stmt.getText(_columnIndexOfColor);
          }
          _item.budgetLimit = _stmt.getDouble(_columnIndexOfBudgetLimit);
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

package Dao;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import entity.User;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<User> __insertAdapterOfUser;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfUser = new EntityInsertAdapter<User>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `users` (`id`,`username`,`email`,`password`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final User entity) {
        statement.bindLong(1, entity.id);
        if (entity.username == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.username);
        }
        if (entity.email == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.email);
        }
        if (entity.password == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.password);
        }
      }
    };
  }

  @Override
  public void insert(final User user) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfUser.insert(_connection, user);
      return null;
    });
  }

  @Override
  public User login(final String username, final String password) {
    final String _sql = "SELECT * FROM users WHERE username = ? AND password = ? LIMIT 1";
    return DBUtil.performBlocking(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (username == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, username);
        }
        _argIndex = 2;
        if (password == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, password);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfUsername = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "username");
        final int _columnIndexOfEmail = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "email");
        final int _columnIndexOfPassword = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "password");
        final User _result;
        if (_stmt.step()) {
          _result = new User();
          _result.id = (int) (_stmt.getLong(_columnIndexOfId));
          if (_stmt.isNull(_columnIndexOfUsername)) {
            _result.username = null;
          } else {
            _result.username = _stmt.getText(_columnIndexOfUsername);
          }
          if (_stmt.isNull(_columnIndexOfEmail)) {
            _result.email = null;
          } else {
            _result.email = _stmt.getText(_columnIndexOfEmail);
          }
          if (_stmt.isNull(_columnIndexOfPassword)) {
            _result.password = null;
          } else {
            _result.password = _stmt.getText(_columnIndexOfPassword);
          }
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

package entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public double amount;
    public long date; // Timestamp
    public String description;
    public int categoryId;
    public String photoPath;
    public String startTime;
    public String endTime;
}
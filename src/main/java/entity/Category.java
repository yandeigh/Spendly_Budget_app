package entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String icon; // Resource name or emoji
    public String color; // Hex color
    public double budgetLimit;
}



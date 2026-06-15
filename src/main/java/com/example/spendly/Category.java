package com.example.spendly;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import entity.AppDatabase;
import java.util.concurrent.Executors;

public class Category extends AppCompatActivity {

    private AppDatabase db;
    private CategoryAdapter adapter;

    // Emoji options for icon picker
    private static final String[] ICONS = {"🍔", "🚗", "🏠", "💊", "🎮", "✈️", "👗", "📚", "💡", "🎁"};
    // Hex color options for color picker
    private static final String[] COLORS = {
            "#F44336", "#E91E63", "#9C27B0", "#3F51B5",
            "#2196F3", "#009688", "#4CAF50", "#FF9800", "#795548", "#607D8B"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setTitle("Manage Categories");

        db = AppDatabase.getDatabase(this);

        RecyclerView recyclerView = findViewById(R.id.rvCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CategoryAdapter(
                category -> showEditDialog(category),   // on edit
                category -> confirmDelete(category)     // on delete
        );
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAddCategory);
        fab.setOnClickListener(v -> showAddDialog());

        db.categoryDao().getAllCategories().observe(this, categories -> {
            adapter.setCategories(categories);
        });
    }

    private void showAddDialog() {
        showCategoryDialog(null);
    }

    private void showEditDialog(entity.Category category) {
        showCategoryDialog(category);
    }

    private void showCategoryDialog(entity.Category existing) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_category, null);
        EditText etName = dialogView.findViewById(R.id.etCategoryName);
        TextView tvIconPicker = dialogView.findViewById(R.id.tvIconPicker);
        TextView tvColorPicker = dialogView.findViewById(R.id.tvColorPicker);

        // Track selections
        final String[] selectedIcon = {existing != null ? existing.icon : ICONS[0]};
        final String[] selectedColor = {existing != null ? existing.color : COLORS[0]};

        if (existing != null) etName.setText(existing.name);
        tvIconPicker.setText(selectedIcon[0]);
        tvColorPicker.setBackgroundColor(android.graphics.Color.parseColor(selectedColor[0]));

        tvIconPicker.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Pick an icon")
                    .setItems(ICONS, (d, which) -> {
                        selectedIcon[0] = ICONS[which];
                        tvIconPicker.setText(ICONS[which]);
                    })
                    .show();
        });

        tvColorPicker.setOnClickListener(v -> {
            // Build color labels for display
            String[] colorLabels = {"Red","Pink","Purple","Indigo","Blue","Teal","Green","Orange","Brown","Grey"};
            new AlertDialog.Builder(this)
                    .setTitle("Pick a color")
                    .setItems(colorLabels, (d, which) -> {
                        selectedColor[0] = COLORS[which];
                        tvColorPicker.setBackgroundColor(android.graphics.Color.parseColor(COLORS[which]));
                    })
                    .show();
        });

        new AlertDialog.Builder(this)
                .setTitle(existing != null ? "Edit Category" : "New Category")
                .setView(dialogView)
                .setPositiveButton("Save", (d, w) -> {
                    String name = etName.getText().toString().trim();
                    if (name.isEmpty()) {
                        Toast.makeText(this, "Name can't be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    entity.Category category = existing != null ? existing : new entity.Category();
                    category.name = name;
                    category.icon = selectedIcon[0];
                    category.color = selectedColor[0];

                    Executors.newSingleThreadExecutor().execute(() -> {
                        if (existing != null) db.categoryDao().update(category);
                        else db.categoryDao().insert(category);
                        runOnUiThread(() -> Toast.makeText(this,
                                existing != null ? "Category updated" : "Category added",
                                Toast.LENGTH_SHORT).show());
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void confirmDelete(entity.Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Delete \"" + category.name + "\"?")
                .setMessage("This won't delete existing expenses in this category.")
                .setPositiveButton("Delete", (d, w) -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        db.categoryDao().delete(category);
                        runOnUiThread(() -> Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show());
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
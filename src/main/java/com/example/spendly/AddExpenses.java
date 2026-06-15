package com.example.spendly;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import entity.AppDatabase;
import entity.Category;
import entity.Expense;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class AddExpenses extends AppCompatActivity {

    public static final String EXTRA_EXPENSE_ID = "expense_id";

    private EditText etAmount, etDescription;
    private Spinner spCategory;
    private Button btnDatePicker, btnStartTime, btnEndTime, btnSaveExpense, btnAttachPhoto;
    private ImageView ivExpensePhoto;

    private Calendar calendar = Calendar.getInstance();
    private String startTime = "", endTime = "";
    private String photoPath = "";
    private AppDatabase db;

    private List<Category> categoryList = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;

    private int editExpenseId = -1;
    private Expense existingExpense = null;

    private ActivityResultLauncher<Uri> cameraLauncher;
    private Uri photoUri;

    private final SimpleDateFormat dateDisplay = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        db = AppDatabase.getDatabase(this);

        etAmount       = findViewById(R.id.etAmount);
        etDescription  = findViewById(R.id.etDescription);
        spCategory     = findViewById(R.id.spCategory);
        btnDatePicker  = findViewById(R.id.btnDatePicker);
        btnStartTime   = findViewById(R.id.btnStartTime);
        btnEndTime     = findViewById(R.id.btnEndTime);
        btnSaveExpense = findViewById(R.id.btnSaveExpense);
        btnAttachPhoto = findViewById(R.id.btnAttachPhoto);
        ivExpensePhoto = findViewById(R.id.ivExpensePhoto);

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(), success -> {
                    if (success && photoUri != null) {
                        photoPath = photoUri.toString();
                        ivExpensePhoto.setImageURI(photoUri);
                        ivExpensePhoto.setVisibility(android.view.View.VISIBLE);
                    }
                });

        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);

        db.categoryDao().getAllCategories().observe(this, categories -> {
            categoryList = categories != null ? categories : new ArrayList<>();
            categoryAdapter.clear();
            for (Category c : categoryList) categoryAdapter.add(c.icon + " " + c.name);
            categoryAdapter.notifyDataSetChanged();
            if (existingExpense != null) selectCategoryById(existingExpense.categoryId);
        });

        editExpenseId = getIntent().getIntExtra(EXTRA_EXPENSE_ID, -1);
        if (editExpenseId != -1) {
            setTitle("Edit Expense");
            btnSaveExpense.setText("Update Expense");
            loadExpenseForEdit(editExpenseId);
        }

        btnDatePicker.setText(dateDisplay.format(calendar.getTime()));
        btnDatePicker.setOnClickListener(v ->
                new DatePickerDialog(this, (view, year, month, day) -> {
                    calendar.set(year, month, day);
                    btnDatePicker.setText(dateDisplay.format(calendar.getTime()));
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show());

        btnStartTime.setOnClickListener(v ->
                new TimePickerDialog(this, (view, h, m) -> {
                    startTime = String.format(Locale.getDefault(), "%02d:%02d", h, m);
                    btnStartTime.setText("Start: " + startTime);
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show());

        btnEndTime.setOnClickListener(v ->
                new TimePickerDialog(this, (view, h, m) -> {
                    endTime = String.format(Locale.getDefault(), "%02d:%02d", h, m);
                    btnEndTime.setText("End: " + endTime);
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show());

        btnAttachPhoto.setOnClickListener(v -> launchCamera());
        btnSaveExpense.setOnClickListener(v -> saveOrUpdate());
    }

    private void loadExpenseForEdit(int id) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Expense> all = db.expenseDao().getAllExpensesSync();
            for (Expense e : all) {
                if (e.id == id) { existingExpense = e; break; }
            }
            runOnUiThread(() -> {
                if (existingExpense == null) { finish(); return; }

                etAmount.setText(String.valueOf(existingExpense.amount));
                etDescription.setText(existingExpense.description);
                calendar.setTimeInMillis(existingExpense.date);
                btnDatePicker.setText(dateDisplay.format(calendar.getTime()));

                if (existingExpense.startTime != null && !existingExpense.startTime.isEmpty()) {
                    startTime = existingExpense.startTime;
                    btnStartTime.setText("Start: " + startTime);
                }
                if (existingExpense.endTime != null && !existingExpense.endTime.isEmpty()) {
                    endTime = existingExpense.endTime;
                    btnEndTime.setText("End: " + endTime);
                }
                if (existingExpense.photoPath != null && !existingExpense.photoPath.isEmpty()) {
                    photoPath = existingExpense.photoPath;
                    ivExpensePhoto.setImageURI(Uri.parse(photoPath));
                    ivExpensePhoto.setVisibility(android.view.View.VISIBLE);
                }
                selectCategoryById(existingExpense.categoryId);
            });
        });
    }

    private void selectCategoryById(int categoryId) {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).id == categoryId) {
                spCategory.setSelection(i);
                return;
            }
        }
    }

    private void saveOrUpdate() {
        String amountStr = etAmount.getText().toString().trim();
        String desc      = etDescription.getText().toString().trim();

        if (amountStr.isEmpty()) { etAmount.setError("Enter an amount"); return; }
        if (desc.isEmpty())      { etDescription.setError("Enter a description"); return; }

        double amount;
        try { amount = Double.parseDouble(amountStr); }
        catch (NumberFormatException e) { etAmount.setError("Invalid amount"); return; }

        int categoryId = 0;
        if (!categoryList.isEmpty()) {
            categoryId = categoryList.get(spCategory.getSelectedItemPosition()).id;
        }

        final int finalCategoryId = categoryId;

        Executors.newSingleThreadExecutor().execute(() -> {
            if (editExpenseId == -1) {
                Expense expense = new Expense();
                expense.amount      = amount;
                expense.description = desc;
                expense.date        = calendar.getTimeInMillis();
                expense.categoryId  = finalCategoryId;
                expense.startTime   = startTime;
                expense.endTime     = endTime;
                expense.photoPath   = photoPath;
                db.expenseDao().insert(expense);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Expense saved!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } else {
                existingExpense.amount      = amount;
                existingExpense.description = desc;
                existingExpense.date        = calendar.getTimeInMillis();
                existingExpense.categoryId  = finalCategoryId;
                existingExpense.startTime   = startTime;
                existingExpense.endTime     = endTime;
                existingExpense.photoPath   = photoPath;
                db.expenseDao().update(existingExpense);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Expense updated!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private void launchCamera() {
        try {
            File photoFile = File.createTempFile("expense_", ".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            photoUri = FileProvider.getUriForFile(this,
                    getPackageName() + ".fileprovider", photoFile);
            cameraLauncher.launch(photoUri);
        } catch (IOException e) {
            Toast.makeText(this, "Could not open camera", Toast.LENGTH_SHORT).show();
        }
    }
}
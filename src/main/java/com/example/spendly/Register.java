package com.example.spendly;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import entity.AppDatabase;
import entity.User;
import java.util.concurrent.Executors;

public class Register extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnRegister;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Reusing layout for simplicity or creating activity_register.xml

        db = AppDatabase.getDatabase(this);
        // Assuming we update activity_login to have an email field or create a new layout
        // For this prototype, let's just use username and password
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setText("Register");

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User();
            newUser.username = username;
            newUser.password = password;

            Executors.newSingleThreadExecutor().execute(() -> {
                db.userDao().insert(newUser);
                runOnUiThread(() -> {
                    Toast.makeText(this, "User registered!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });

    }
}

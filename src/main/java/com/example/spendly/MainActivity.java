package com.example.spendly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import entity.AppDatabase;
import entity.User;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Auto-login if session exists
        SharedPreferences prefs = getSharedPreferences("SpendlyPrefs", MODE_PRIVATE);
        String savedUsername = prefs.getString("USERNAME", null);
        if (savedUsername != null) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.putExtra("USERNAME", savedUsername);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        db = AppDatabase.getDatabase(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                User loggedUser = db.userDao().login(username, password);
                runOnUiThread(() -> {
                    if (loggedUser != null) {
                        prefs.edit().putString("USERNAME", loggedUser.username).apply();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        intent.putExtra("USERNAME", loggedUser.username);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        tvRegister.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Register.class)));
    }
}

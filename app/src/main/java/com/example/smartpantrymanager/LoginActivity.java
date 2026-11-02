package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            if (!validate()) return;
            String u = etUsername.getText().toString().trim();
            String p = etPassword.getText().toString();
            if (db.checkLogin(u, p)) {
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra("username", u);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            if (!validate()) return;
            String u = etUsername.getText().toString().trim();
            String p = etPassword.getText().toString();
            if (db.registerUser(u, p)) {
                Toast.makeText(this, "Account created. Sign in now.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate() {
        if (etUsername.getText().toString().trim().isEmpty()) {
            etUsername.setError("Username required");
            return false;
        }
        if (etPassword.getText().toString().length() < 6) {
            etPassword.setError("At least 6 characters");
            return false;
        }
        return true;
    }
}

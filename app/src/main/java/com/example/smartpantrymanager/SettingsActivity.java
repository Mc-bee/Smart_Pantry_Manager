package com.example.smartpantrymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences p = getSharedPreferences("settings", MODE_PRIVATE);

        SwitchCompat sw = findViewById(R.id.swAlerts);
        sw.setChecked(p.getBoolean("expiry_alerts", false));
        sw.setOnCheckedChangeListener((b, on) ->
                p.edit().putBoolean("expiry_alerts", on).apply());

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });
        NavHelper.setup(this, R.id.nav_settings);
    }
}

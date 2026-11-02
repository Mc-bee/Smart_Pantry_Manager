package com.example.smartpantrymanager;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavHelper {
    public static void setup(Activity a, int selectedId) {
        BottomNavigationView nav = a.findViewById(R.id.bottomNav);
        nav.setSelectedItemId(selectedId);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedId) return true;
            Class<?> target = id == R.id.nav_pantry ? MainActivity.class
                    : id == R.id.nav_suggested ? SuggestedActivity.class
                    : SettingsActivity.class;
            a.startActivity(new Intent(a, target));
            a.finish();
            return true;
        });
    }
}

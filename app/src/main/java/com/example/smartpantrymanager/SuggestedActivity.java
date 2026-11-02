package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SuggestedActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private ListView lv;
    private TextView tvNone;
    private List<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested);
        db = new DatabaseHelper(this);
        lv = findViewById(R.id.lvRecipes);
        tvNone = findViewById(R.id.tvNone);
        lv.setOnItemClickListener((p, v, pos, id) -> {
            Intent i = new Intent(this, RecipeDetailActivity.class);
            i.putExtra("recipe_id", recipes.get(pos).getId());
            startActivity(i);
        });
        NavHelper.setup(this, R.id.nav_suggested);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recipes = RecipeMatcher.suggest(db);
        List<String> names = new ArrayList<>();
        for (Recipe r : recipes) names.add(r.getName());
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names));
        tvNone.setVisibility(recipes.isEmpty() ? View.VISIBLE : View.GONE);
    }
}

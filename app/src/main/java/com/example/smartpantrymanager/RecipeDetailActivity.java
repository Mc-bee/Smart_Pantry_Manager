package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        DatabaseHelper db = new DatabaseHelper(this);
        int id = getIntent().getIntExtra("recipe_id", -1);
        Recipe r = db.getRecipe(id);
        if (r == null) { finish(); return; }

        ((TextView) findViewById(R.id.tvTitle)).setText(r.getName());
        StringBuilder sb = new StringBuilder();
        for (RecipeIngredient ri : db.getIngredients(id))
            sb.append("• ").append(ri.getName()).append(" - ")
                    .append(ri.getQuantity()).append(" ").append(ri.getUnit()).append("\n");
        ((TextView) findViewById(R.id.tvIngredients)).setText(sb.toString());
        ((TextView) findViewById(R.id.tvSteps)).setText(r.getSteps());
    }
}

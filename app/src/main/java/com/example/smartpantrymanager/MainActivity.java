package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PantryAdapter.Listener {
    private DatabaseHelper db;
    private PantryAdapter adapter;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        tvEmpty = findViewById(R.id.tvEmpty);

        RecyclerView rv = findViewById(R.id.rvPantry);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(new ArrayList<>(), this);
        rv.setAdapter(adapter);

        findViewById(R.id.btnAdd).setOnClickListener(v ->
                startActivity(new Intent(this, AddEditActivity.class)));

        NavHelper.setup(this, R.id.nav_pantry);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        List<PantryItem> items = db.getAllPantryItems();
        adapter.setItems(items);
        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
        checkExpiry(items);
    }

    private void checkExpiry(List<PantryItem> items) {
        if (!getSharedPreferences("settings", MODE_PRIVATE)
                .getBoolean("expiry_alerts", false)) return;
        long limit = System.currentTimeMillis() + 3L * 24 * 60 * 60 * 1000;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        int n = 0;
        for (PantryItem i : items) {
            try {
                if (i.getExpiry() != null && !i.getExpiry().isEmpty()
                        && f.parse(i.getExpiry()).getTime() <= limit) n++;
            } catch (ParseException ignored) {
            }
        }
        if (n > 0) Toast.makeText(this, n + " item(s) expiring soon",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEdit(PantryItem item) {
        Intent i = new Intent(this, AddEditActivity.class);
        i.putExtra("id", item.getId());
        i.putExtra("name", item.getName());
        i.putExtra("qty", item.getQuantity());
        i.putExtra("unit", item.getUnit());
        i.putExtra("expiry", item.getExpiry());
        startActivity(i);
    }

    @Override
    public void onDelete(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete " + item.getName() + "?")
                .setPositiveButton("Delete", (d, w) -> {
                    db.deletePantryItem(item.getId());
                    refresh();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

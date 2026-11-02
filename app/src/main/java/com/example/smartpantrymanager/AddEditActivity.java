package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {
    private static final String[] UNITS = {"g", "kg", "ml", "l", "pcs", "tsp", "tbsp", "cup"};
    private EditText etName, etQty, etExpiry;
    private Spinner spUnit;
    private DatabaseHelper db;
    private int editId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etName);
        etQty = findViewById(R.id.etQty);
        etExpiry = findViewById(R.id.etExpiry);
        spUnit = findViewById(R.id.spUnit);
        spUnit.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, UNITS));

        Intent in = getIntent();
        if (in.hasExtra("id")) {
            editId = in.getIntExtra("id", -1);
            etName.setText(in.getStringExtra("name"));
            etQty.setText(String.valueOf(in.getDoubleExtra("qty", 0)));
            etExpiry.setText(in.getStringExtra("expiry"));
            String unit = in.getStringExtra("unit");
            for (int i = 0; i < UNITS.length; i++)
                if (UNITS[i].equals(unit)) spUnit.setSelection(i);
            setTitle("Edit Ingredient");
        } else {
            setTitle("Add Ingredient");
        }

        findViewById(R.id.btnSave).setOnClickListener(v -> save());
    }

    private void save() {
        String name = etName.getText().toString().trim();
        String qtyText = etQty.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        if (name.isEmpty()) { etName.setError("Name required"); return; }
        double qty;
        try {
            qty = Double.parseDouble(qtyText);
        } catch (NumberFormatException e) {
            etQty.setError("Enter a number");
            return;
        }
        if (qty <= 0) { etQty.setError("Must be greater than 0"); return; }
        if (!expiry.isEmpty() && !expiry.matches("\\d{4}-\\d{2}-\\d{2}")) {
            etExpiry.setError("Use yyyy-mm-dd");
            return;
        }

        String unit = spUnit.getSelectedItem().toString();
        if (editId == -1) db.addPantryItem(name, qty, unit, expiry);
        else db.updatePantryItem(editId, name, qty, unit, expiry);
        finish();
    }
}

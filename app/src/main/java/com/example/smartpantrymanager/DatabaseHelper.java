package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "pantry.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT, expiry TEXT)");
        db.execSQL("CREATE TABLE recipes (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, steps TEXT)");
        db.execSQL("CREATE TABLE recipe_ingredients (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER, name TEXT NOT NULL, quantity REAL, unit TEXT, " +
                "FOREIGN KEY(recipe_id) REFERENCES recipes(id))");
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE NOT NULL, password_hash TEXT NOT NULL)");
        RecipeSeeder.seed(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS pantry");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // ---------- Users ----------
    public boolean registerUser(String username, String password) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password_hash", hash(password));
        return getWritableDatabase().insert("users", null, cv) != -1;
    }

    public boolean checkLogin(String username, String password) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id FROM users WHERE username=? AND password_hash=?",
                new String[]{username, hash(password)});
        boolean ok = c.moveToFirst();
        c.close();
        return ok;
    }

    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------- Pantry CRUD ----------
    public long addPantryItem(String name, double qty, String unit, String expiry) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("quantity", qty);
        cv.put("unit", unit);
        cv.put("expiry", expiry);
        return getWritableDatabase().insert("pantry", null, cv);
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id, name, quantity, unit, expiry FROM pantry ORDER BY name", null);
        while (c.moveToNext()) {
            list.add(new PantryItem(c.getInt(0), c.getString(1),
                    c.getDouble(2), c.getString(3), c.getString(4)));
        }
        c.close();
        return list;
    }

    public int updatePantryItem(int id, String name, double qty, String unit, String expiry) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("quantity", qty);
        cv.put("unit", unit);
        cv.put("expiry", expiry);
        return getWritableDatabase().update("pantry", cv, "id=?",
                new String[]{String.valueOf(id)});
    }

    public int deletePantryItem(int id) {
        return getWritableDatabase().delete("pantry", "id=?",
                new String[]{String.valueOf(id)});
    }

    // ---------- Recipes ----------
    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id, name, steps FROM recipes ORDER BY name", null);
        while (c.moveToNext())
            list.add(new Recipe(c.getInt(0), c.getString(1), c.getString(2)));
        c.close();
        return list;
    }

    public Recipe getRecipe(int id) {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT id, name, steps FROM recipes WHERE id=?",
                new String[]{String.valueOf(id)});
        Recipe r = null;
        if (c.moveToFirst()) r = new Recipe(c.getInt(0), c.getString(1), c.getString(2));
        c.close();
        return r;
    }

    public List<RecipeIngredient> getIngredients(int recipeId) {
        List<RecipeIngredient> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT name, quantity, unit FROM recipe_ingredients WHERE recipe_id=?",
                new String[]{String.valueOf(recipeId)});
        while (c.moveToNext())
            list.add(new RecipeIngredient(c.getString(0), c.getDouble(1), c.getString(2)));
        c.close();
        return list;
    }
}

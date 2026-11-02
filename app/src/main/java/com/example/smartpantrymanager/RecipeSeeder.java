package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class RecipeSeeder {

    public static void seed(SQLiteDatabase db) {
        add(db, "Tomato Omelette", "1. Beat the eggs.\n2. Chop the tomato.\n3. Fry everything in oil until set.",
                "egg|2|pcs", "tomato|1|pcs", "oil|1|tbsp");
        add(db, "Buttered Rice", "1. Boil the rice until soft.\n2. Drain and stir in the butter.",
                "rice|200|g", "butter|20|g");
        add(db, "Pancakes", "1. Mix flour, milk and egg into a batter.\n2. Fry spoonfuls in butter until golden on both sides.",
                "flour|200|g", "milk|250|ml", "egg|1|pcs", "butter|10|g");
        add(db, "Cheese Toast", "1. Top the bread with cheese.\n2. Grill until the cheese melts.",
                "bread|2|pcs", "cheese|50|g");
        add(db, "Garlic Pasta", "1. Boil the pasta.\n2. Fry the garlic in oil.\n3. Toss the pasta in the garlic oil.",
                "pasta|200|g", "garlic|2|pcs", "oil|2|tbsp");
        add(db, "Scrambled Eggs", "1. Beat eggs with milk.\n2. Melt butter in a pan.\n3. Stir gently until just set.",
                "egg|3|pcs", "butter|10|g", "milk|50|ml");
        add(db, "Egg Fried Rice", "1. Fry the chopped onion in oil.\n2. Add rice and stir.\n3. Push aside, scramble the eggs, mix together.",
                "rice|200|g", "egg|2|pcs", "oil|1|tbsp", "onion|1|pcs");
        add(db, "Tomato Soup", "1. Fry the onion in butter.\n2. Add chopped tomatoes and simmer 20 minutes.\n3. Blend until smooth.",
                "tomato|4|pcs", "onion|1|pcs", "butter|20|g");
        add(db, "Mashed Potatoes", "1. Boil the potatoes until soft.\n2. Mash with butter and warm milk.",
                "potato|500|g", "butter|30|g", "milk|100|ml");
        add(db, "French Toast", "1. Whisk eggs and milk.\n2. Soak the bread.\n3. Fry in butter until golden.",
                "bread|4|pcs", "egg|2|pcs", "milk|100|ml", "butter|10|g");
        add(db, "Cheese Omelette", "1. Beat the eggs.\n2. Cook in butter.\n3. Add cheese and fold.",
                "egg|3|pcs", "cheese|40|g", "butter|10|g");
        add(db, "Tuna Pasta", "1. Boil the pasta.\n2. Fry garlic in oil.\n3. Add tuna and toss with the pasta.",
                "pasta|200|g", "tuna|1|pcs", "oil|1|tbsp", "garlic|1|pcs");
        add(db, "Pasta Pomodoro", "1. Boil the pasta.\n2. Simmer chopped tomato with garlic in oil.\n3. Mix with the pasta.",
                "pasta|200|g", "tomato|3|pcs", "garlic|2|pcs", "oil|2|tbsp");
        add(db, "Banana Pancakes", "1. Mash bananas.\n2. Mix in eggs and flour.\n3. Fry small pancakes.",
                "banana|2|pcs", "egg|2|pcs", "flour|50|g");
        add(db, "Porridge", "1. Heat oats and milk together.\n2. Stir until thick.\n3. Sweeten with sugar.",
                "oats|80|g", "milk|300|ml", "sugar|1|tbsp");
        add(db, "Garlic Bread", "1. Mash garlic into butter.\n2. Spread on the bread.\n3. Bake until crisp.",
                "bread|4|pcs", "butter|40|g", "garlic|2|pcs");
        add(db, "Egg Sandwich", "1. Fry the eggs in butter.\n2. Place between the slices of bread.",
                "bread|2|pcs", "egg|2|pcs", "butter|10|g");
    }

    private static void add(SQLiteDatabase db, String name, String steps, String... ingredients) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("steps", steps);
        long id = db.insert("recipes", null, cv);
        for (String s : ingredients) {
            String[] p = s.split("\\|");
            ContentValues iv = new ContentValues();
            iv.put("recipe_id", id);
            iv.put("name", p[0]);
            iv.put("quantity", Double.parseDouble(p[1]));
            iv.put("unit", p[2]);
            db.insert("recipe_ingredients", null, iv);
        }
    }
}

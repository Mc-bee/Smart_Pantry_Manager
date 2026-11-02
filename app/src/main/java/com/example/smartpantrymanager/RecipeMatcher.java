package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;

public class RecipeMatcher {

    // "Tomatoes " -> "tomato", "Berries" -> "berry"
    public static String normName(String n) {
        n = n.trim().toLowerCase();
        if (n.endsWith("ies") && n.length() > 4) return n.substring(0, n.length() - 3) + "y";
        if (n.endsWith("oes")) return n.substring(0, n.length() - 2);
        if (n.endsWith("s") && !n.endsWith("ss")) return n.substring(0, n.length() - 1);
        return n;
    }

    // kg -> g and l -> ml so units can be compared
    private static String baseUnit(String u) {
        u = u == null ? "" : u.trim().toLowerCase();
        if (u.equals("kg")) return "g";
        if (u.equals("l")) return "ml";
        return u;
    }

    private static double toBase(double q, String u) {
        u = u == null ? "" : u.trim().toLowerCase();
        if (u.equals("kg") || u.equals("l")) return q * 1000;
        return q;
    }

    // True only if the pantry holds enough of this ingredient
    private static boolean hasEnough(List<PantryItem> pantry, RecipeIngredient need) {
        String name = normName(need.getName());
        String unit = baseUnit(need.getUnit());
        double total = 0;
        for (PantryItem p : pantry) {
            if (normName(p.getName()).equals(name) && baseUnit(p.getUnit()).equals(unit))
                total += toBase(p.getQuantity(), p.getUnit());
        }
        return total >= toBase(need.getQuantity(), need.getUnit());
    }

    // Strict rule: EVERY ingredient must be present in enough quantity
    public static List<Recipe> suggest(DatabaseHelper db) {
        List<PantryItem> pantry = db.getAllPantryItems();
        List<Recipe> result = new ArrayList<>();
        for (Recipe r : db.getAllRecipes()) {
            boolean ok = true;
            for (RecipeIngredient ri : db.getIngredients(r.getId())) {
                if (!hasEnough(pantry, ri)) { ok = false; break; }
            }
            if (ok) result.add(r);
        }
        return result;
    }
}

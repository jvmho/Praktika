package com.example.vitaminka.managers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FavoritesManager {
    private static final String PREFS_NAME = "favorites";
    private static final String KEY_FAVORITES = "favorite_ids";
    private static FavoritesManager instance;
    private final SharedPreferences prefs;

    public FavoritesManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized FavoritesManager getInstance(Context context) {
        if (instance == null) {
            instance = new FavoritesManager(context);
        }
        return instance;
    }

    public Set<Integer> getFavoriteIds() {
        String saved = prefs.getString(KEY_FAVORITES, "");
        if (saved.isEmpty()) return new HashSet<>();
        return Arrays.stream(saved.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toSet());
    }

    public boolean isFavorite(int drugId) {
        return getFavoriteIds().contains(drugId);
    }

    public void toggleFavorite(int drugId) {
        Set<Integer> ids = getFavoriteIds();
        if (ids.contains(drugId)) {
            ids.remove(drugId);
        } else {
            ids.add(drugId);
        }
        prefs.edit().putString(KEY_FAVORITES, ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","))).apply();
    }
}
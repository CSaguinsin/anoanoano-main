package com.example.budgettracker;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesHelper {

    private static final String PREF_NAME = "ExpenseTrackerPrefs";
    private static final String KEY_EXPENSES = "expenses";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveExpenses(Context context, List<Expense> expenses) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        Gson gson = new Gson();
        String json = gson.toJson(expenses);
        editor.putString(KEY_EXPENSES, json);
        editor.apply();
    }

    public static List<Expense> getExpenses(Context context) {
        Gson gson = new Gson();
        String json = getSharedPreferences(context).getString(KEY_EXPENSES, null);
        Type type = new TypeToken<ArrayList<Expense>>() {}.getType();
        return gson.fromJson(json, type);
    }
}

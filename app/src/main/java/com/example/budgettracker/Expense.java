package com.example.budgettracker;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Expense {
    private double amount;
    private String category;
    private String note;
    private String date;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void addToFirebase() {
        // Get Firebase database reference
        DatabaseReference expensesRef = FirebaseDatabase.getInstance().getReference("expenses");

        // Create a unique key for the expense
        String expenseId = expensesRef.push().getKey();

        // Create a HashMap to hold expense data
        Map<String, Object> expenseData = new HashMap<>();
        expenseData.put("amount", amount);
        expenseData.put("category", category);
        expenseData.put("note", note);
        expenseData.put("date", date); // Convert Date object to milliseconds

        // Add expense data to database under the generated key
        expensesRef.child(expenseId).setValue(expenseData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Expense added to database successfully");
                } else {
                    Log.e(TAG, "Failed to add expense to database: " + task.getException().getMessage());
                }
            }
        });
    }
}


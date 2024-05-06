package com.example.budgettracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExportActivity extends AppCompatActivity {

    private DatabaseReference goalsRef;
    private List<Goal> goalsList = new ArrayList<>();
    private List<Expense> expenseList = new ArrayList<>();
    private DatabaseReference expensesRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        expensesRef = database.getReference("expenses");
        goalsRef = database.getReference("goals");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        retrieveExpenses(expenseList);
        loadGoals();
        //para sa title
        //getSupportActionBar().setTitle("Export");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Budget Tracker Software");
        }

        //para sa login
        Button btnExportExpenses = findViewById(R.id.btn_export_expenses);
        Button btnExportGoals = findViewById(R.id.btn_export_goals);
        btnExportExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CSVGenerator.generateExpenseCSV(ExportActivity.this, expenseList);
            }
        });
        btnExportGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CSVGenerator.generateGoalCSV(ExportActivity.this, goalsList);
            }
        });

    }
    private void loadGoals() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Handle not signed in user
            return;
        }

        goalsRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                goalsList.clear(); // Clear previous goals

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Goal goal = dataSnapshot.getValue(Goal.class);
                    if (goal != null) {
                        goalsList.add(goal);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Log.e("YourActivity", "Failed to load goals: " + error.getMessage());
            }
        });
    }

    private void retrieveExpenses(final List<Expense> expenseList) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Handle not signed in user
            return;
        }

        DatabaseReference userExpensesRef = expensesRef.child(currentUser.getUid());
        userExpensesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                    Expense expense = expenseSnapshot.getValue(Expense.class);
                    if (expense != null) {
                        expenseList.add(expense);
                    }
                }
                // Now you have all expenses in expenseList
                // Do whatever you need with the list, like updating UI
// Iterate over the list of expenses
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Log.e("S", "Failed to retrieve expenses: " + databaseError.getMessage());
            }
        });
    }

}
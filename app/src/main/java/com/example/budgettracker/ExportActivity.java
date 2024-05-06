package com.example.budgettracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private FirebaseAuth authProfile;
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

    public boolean onCreateOptionsMenu(Menu menu){
        //inflate menu items
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(ExportActivity.this);
            finish();
        } else if (id == R.id.menu_refresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        } else if (id == R.id.menu_update_profile) {
            Intent intent = new Intent (ExportActivity.this, UpdateProfile.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_profile) {
            Intent intent = new Intent (ExportActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_update_email) {
            Intent intent = new Intent (ExportActivity.this, UpdateEmail.class);
            startActivity(intent);
        } else if (id == R.id.menu_change_pass) {
            Intent intent = new Intent (ExportActivity.this, ChangePassword.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_delete) {
            Intent intent = new Intent (ExportActivity.this, DeleteUser.class);
            startActivity(intent);
        } else if (id == R.id.menu_home) {
            Intent intent = new Intent(ExportActivity.this, DashboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_goals) {
            Intent intent = new Intent(ExportActivity.this, GoalActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_export) {
            Intent intent = new Intent(ExportActivity.this, ExportActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(ExportActivity.this, "Logged Out", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ExportActivity.this, MainActivity.class);

            //clear stack instance & close activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(ExportActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
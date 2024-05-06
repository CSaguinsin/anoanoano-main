package com.example.budgettracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Dashboard");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Initialize the swipeContainer
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeToRefresh();

        LinearLayout layout = findViewById(R.id.dashboardLayout);

        List<Expense> expenses = SharedPreferencesHelper.getExpenses(this);

        if (expenses != null) {
            // Sort expenses by date (assuming Expense class has a getDate() method)
            Collections.sort(expenses, new Comparator<Expense>() {
                @Override
                public int compare(Expense e1, Expense e2) {
                    return e1.getDate().compareTo(e2.getDate());
                }
            });

            // Display expenses with delete buttons
            for (Expense expense : expenses) {
                TextView textView = new TextView(this);
                textView.setText("Amount: PHP" + expense.getAmount() + "\nCategory: " + expense.getCategory() + "\nNote: " + expense.getNote() + "\nDate: " + expense.getDate());

                Button deleteButton = new Button(this);
                deleteButton.setText("Delete");
                deleteButton.setOnClickListener(v -> {
                    // Remove expense from list
                    expenses.remove(expense);
                    // Update SharedPreferences
                    SharedPreferencesHelper.saveExpenses(DashboardActivity.this, expenses);
                    // Remove views from layout
                    layout.removeView(textView);
                    layout.removeView(deleteButton);
                    // Notify user
                    Toast.makeText(DashboardActivity.this, "Expense deleted", Toast.LENGTH_SHORT).show();
                });

                layout.addView(textView);
                layout.addView(deleteButton);
            }
        }


        // Add Expense button click listener
        Button addExpenseButton = findViewById(R.id.buttonAddExpense);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to AddExpenseActivity
                Intent intent = new Intent(DashboardActivity.this, AddExpenseActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDashboard();
    }

    private void refreshDashboard() {
        LinearLayout layout = findViewById(R.id.dashboardLayout);
        View addExpenseButton = layout.findViewWithTag("addExpenseButton");
        layout.removeAllViews();

        List<Expense> expenses = SharedPreferencesHelper.getExpenses(this);
        if (expenses == null) {
            expenses = new ArrayList<>();
        }

        Collections.sort(expenses, new Comparator<Expense>() {
            @Override
            public int compare(Expense e1, Expense e2) {
                return e1.getDate().compareTo(e2.getDate());
            }
        });

        if (addExpenseButton != null) {
            layout.addView(addExpenseButton);
        } else {
            // If the button is null, create a new one and add it to the layout
            Button newAddExpenseButton = new Button(this);
            newAddExpenseButton.setText("Add Expense");
            newAddExpenseButton.setTag("addExpenseButton");
            newAddExpenseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redirect to AddExpenseActivity
                    Intent intent = new Intent(DashboardActivity.this, AddExpenseActivity.class);
                    startActivity(intent);
                }
            });
            layout.addView(newAddExpenseButton);
        }

        for (Expense expense : expenses) {
            TextView textView = new TextView(this);
            textView.setText("Amount: PHP" + expense.getAmount() + "\nCategory: " + expense.getCategory() + "\nNote: " + expense.getNote() + "\nDate: " + expense.getDate());

            Button deleteButton = new Button(this);
            deleteButton.setText("Delete");
            List<Expense> finalExpenses = expenses;
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalExpenses.remove(expense);
                    SharedPreferencesHelper.saveExpenses(DashboardActivity.this, finalExpenses);
                    layout.removeView(textView);
                    layout.removeView(deleteButton);
                    Toast.makeText(DashboardActivity.this, "Expense deleted", Toast.LENGTH_SHORT).show();
                }
            });

            layout.addView(textView);
            layout.addView(deleteButton);
        }
    }

    private void swipeToRefresh() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDashboard();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(DashboardActivity.this);
            finish();
        } else if (id == R.id.menu_refresh) {
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        } else if (id == R.id.menu_update_profile) {
            Intent intent = new Intent(DashboardActivity.this, UpdateProfile.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_profile) {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_update_email) {
            Intent intent = new Intent(DashboardActivity.this, UpdateEmail.class);
            startActivity(intent);
        } else if (id == R.id.menu_change_pass) {
            Intent intent = new Intent(DashboardActivity.this, ChangePassword.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_delete) {
            Intent intent = new Intent(DashboardActivity.this, DeleteUser.class);
            startActivity(intent);
        } else if (id == R.id.menu_goals) {
            Intent intent = new Intent(DashboardActivity.this, GoalActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_home) {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.menu_export) {
            Intent intent = new Intent(DashboardActivity.this, ExportActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(DashboardActivity.this, "Logged Out", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(DashboardActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}

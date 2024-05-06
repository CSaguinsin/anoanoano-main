package com.example.budgettracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import java.text.*;
import java.util.*;

public class GoalActivity extends AppCompatActivity {
    private EditText goalNameEditText;
    private EditText targetAmountEditText;
    private Button pickDateButton;
    private EditText selectedDateEditText;
    private Spinner categorySpinner;
    private Button addGoalButton;
    private RecyclerView goalsRecyclerView;
    private GoalAdapter goalAdapter;
    private FirebaseAuth authProfile;
    private Calendar selectedDate;
    private DatabaseReference goalsRef;
    private List<Goal> goalsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goals);

        // Initialize Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        goalsRef = database.getReference("goals");
        goalNameEditText = findViewById(R.id.editTextGoalName);
        targetAmountEditText = findViewById(R.id.editTextTargetAmount);
        pickDateButton = findViewById(R.id.buttonPickDate);
        selectedDateEditText = findViewById(R.id.textViewSelectedDate);
        categorySpinner = findViewById(R.id.spinnerCategory);
        addGoalButton = findViewById(R.id.buttonAddGoal);
        goalsRecyclerView = findViewById(R.id.goalsRecyclerView);

        goalsList = new ArrayList<>();
        goalAdapter = new GoalAdapter(goalsList);
        goalsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        goalsRecyclerView.setAdapter(goalAdapter);

        // Set up category spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.expense_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoal();
            }
        });

        // Load goals from Firebase
        loadGoals();
    }

    private void showDatePickerDialog() {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, monthOfYear);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Update the selected date in the view
                        updateDateInView();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void updateDateInView() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        selectedDateEditText.setText(sdf.format(selectedDate.getTime()));
    }

    private void addGoal() {
        String goalName = goalNameEditText.getText().toString().trim();
        String targetAmountStr = targetAmountEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        if (goalName.isEmpty()) {
            Toast.makeText(this, "Enter goal name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (targetAmountStr.isEmpty()) {
            Toast.makeText(this, "Enter target amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double targetAmount = Double.parseDouble(targetAmountStr);

        if (selectedDate == null) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Handle not signed in user
            return;
        }

        Goal goal = new Goal(goalName, targetAmount, selectedDateEditText.getText().toString(), category);

        goalsRef.child(currentUser.getUid()).push().setValue(goal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(GoalActivity.this, "Goal added successfully", Toast.LENGTH_SHORT).show();
                        // Clear input fields after adding goal
                        goalNameEditText.setText("");
                        targetAmountEditText.setText("");
                        selectedDateEditText.setText("");
                        selectedDate = null;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GoalActivity.this, "Failed to add goal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadGoals() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // Handle not signed in user
            return;
        }

        goalsRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                goalsList.clear(); // Clear previous goals

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Goal goal = dataSnapshot.getValue(Goal.class);
                    if (goal != null) {
                        goal.setProgress(80);
                        goalsList.add(goal);
                    }
                }

                // Notify adapter that data set has changed
                goalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GoalActivity.this, "Failed to load goals: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
            NavUtils.navigateUpFromSameTask(GoalActivity.this);
            finish();
        } else if (id == R.id.menu_refresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        } else if (id == R.id.menu_update_profile) {
            Intent intent = new Intent (GoalActivity.this, UpdateProfile.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_profile) {
            Intent intent = new Intent (GoalActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_update_email) {
            Intent intent = new Intent (GoalActivity.this, UpdateEmail.class);
            startActivity(intent);
        } else if (id == R.id.menu_change_pass) {
            Intent intent = new Intent (GoalActivity.this, ChangePassword.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_delete) {
            Intent intent = new Intent (GoalActivity.this, DeleteUser.class);
            startActivity(intent);
        } else if (id == R.id.menu_home) {
            Intent intent = new Intent(GoalActivity.this, DashboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_goals) {
            Intent intent = new Intent(GoalActivity.this, GoalActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(GoalActivity.this, "Logged Out", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(GoalActivity.this, MainActivity.class);

            //clear stack instance & close activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(GoalActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}

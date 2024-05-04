package com.example.budgettracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText expenseAmountEditText;
    private Spinner expenseCategorySpinner;
    private EditText expenseNoteEditText;
    private Button pickDateButton;
    private TextView textViewSelectedDate;
    private Button cancelButton;
    private Button addExpenseButton;

    private Calendar selectedDate;
    private DatabaseReference expensesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Initialize Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        expensesRef = database.getReference("expenses");

        expenseAmountEditText = findViewById(R.id.editTextExpenseAmount);
        expenseCategorySpinner = findViewById(R.id.spinnerExpenseCategory);
        expenseNoteEditText = findViewById(R.id.editTextExpenseNote);
        pickDateButton = findViewById(R.id.buttonPickDate);
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        cancelButton = findViewById(R.id.buttonCancel);
        addExpenseButton = findViewById(R.id.buttonAddExpense);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.expense_categories, R.layout.spinner_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseCategorySpinner.setAdapter(adapter);

        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        textViewSelectedDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed for this implementation
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Validate the text format when the user finishes typing
                String dateStr = s.toString();
                if (isValidDateFormat(dateStr)) {
                    // If the format is valid, update the selectedDate calendar
                    updateSelectedDateFromString(dateStr);
                } else {
                    // If the format is not valid, clear the selectedDate calendar
                    selectedDate = null;
                    Toast.makeText(AddExpenseActivity.this, "Invalid date format. Please enter date in mm/dd/yyyy format.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to DashboardActivity
                Intent intent = new Intent(AddExpenseActivity.this, DashboardActivity.class);
                startActivity(intent);
                // Finish the current activity
                finish();
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
            }
        });
    }

    // Validate the date format
    private boolean isValidDateFormat(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Update selectedDate from a string
    private void updateSelectedDateFromString(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date date = sdf.parse(dateStr);
            if (date != null) {
                selectedDate = Calendar.getInstance();
                selectedDate.setTime(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

                        // Set the selected date to the expense object
                        updateDateInView();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    // Update the selected date in the view
    private void updateDateInView() {
        String myFormat = "MM/dd/yyyy"; // Change as you need
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        // Apply Singapore timezone
        TimeZone singaporeTimeZone = TimeZone.getTimeZone("Asia/Singapore");
        sdf.setTimeZone(singaporeTimeZone);

        // Format the selected date
        String formattedDate = sdf.format(selectedDate.getTime());

        // Update the textViewSelectedDate with the selected date formatted with Singapore time
        textViewSelectedDate.setText(formattedDate);
        // Make the textViewSelectedDate visible
        textViewSelectedDate.setVisibility(View.VISIBLE);
    }

    private void addExpense() {
        // Retrieve entered expense details
        String amountStr = expenseAmountEditText.getText().toString().trim();
        String category = expenseCategorySpinner.getSelectedItem().toString();
        String note = expenseNoteEditText.getText().toString().trim();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        // Check if selectedDate is null
        if (selectedDate == null) {
            Toast.makeText(this, "Please select a valid date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // User is not signed in, handle appropriately
            return;
        }

        // Create an Expense object
        Expense expense = new Expense();
        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setNote(note);
        expense.setDate(selectedDate.getTime().toString()); // Set the selected date

        // Save the expense to Firebase
        expensesRef.child(currentUser.getUid()).push().setValue(expense)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Expense saved successfully
                        Toast.makeText(AddExpenseActivity.this, "Expense added successfully", Toast.LENGTH_SHORT).show();
                        // Go to DashboardActivity
                        Intent intent = new Intent(AddExpenseActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        // Finish the current activity
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error saving expense
                        Toast.makeText(AddExpenseActivity.this, "Failed to add expense: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

package com.example.budgettracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainDashboard extends AppCompatActivity {

    private FirebaseAuth authProfile;
    budgetAdapter adapter;
    private DatabaseReference mbase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Dashboard");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        List<Expense> expensesList = new ArrayList<>();

        // Create an Expense object
        Expense expense1 = new Expense();
        expense1.setAmount(56.67);
        expense1.setCategory("Food");
        expense1.setNote("Ayo koNa");
        expense1.setDate("07/05/2024"); // Set the selected date
        expensesList.add(expense1);

        // Create an Expense object
        Expense expense2 = new Expense();
        expense2.setAmount(420);
        expense2.setCategory("Transpo");
        expense2.setNote("Pamasahe");
        expense2.setDate("07/03/2024"); // Set the selected date
        expensesList.add(expense2);


        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference(firebaseUser.getUid());
        referenceProfile.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Expense newPost = dataSnapshot.getValue(Expense.class);
                expensesList.add(newPost);
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler1);
        recyclerView.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Initialize Firebase
        mbase = FirebaseDatabase.getInstance().getReference();

        // Initialize adapter
        adapter = new budgetAdapter(this, expensesList);

        // Set adapter to RecyclerView
        recyclerView.setAdapter(adapter);

        // Add Expense button click listener
        Button addExpenseButton = findViewById(R.id.buttonAddExpense);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainDashboard.this, AddExpenseActivity.class);
                startActivity(intent);
            }
        });

    }

}


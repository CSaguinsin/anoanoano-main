package com.example.budgettracker;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private RecyclerView recyclerView;
    private budgetAdapter adapter;
    private DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Dashboard");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase
        mbase = FirebaseDatabase.getInstance().getReference();

        // Set up FirebaseRecyclerOptions
        FirebaseRecyclerOptions<BudgetHistory> options =
                new FirebaseRecyclerOptions.Builder<BudgetHistory>()
                        .setQuery(mbase, BudgetHistory.class)
                        .build();

        // Initialize adapter
        adapter = new budgetAdapter(options);

        // Set adapter to RecyclerView
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}


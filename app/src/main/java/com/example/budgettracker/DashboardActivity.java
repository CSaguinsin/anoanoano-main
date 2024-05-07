package com.example.budgettracker;

import android.os.Bundle;
<<<<<<< Updated upstream

=======
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
>>>>>>> Stashed changes
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
<<<<<<< Updated upstream
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
=======
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
>>>>>>> Stashed changes

public class DashboardActivity extends AppCompatActivity {

    ListView myListView;

    ArrayList<String> myArrayList = new ArrayList<>();

    DatabaseReference mRef;

    private FirebaseAuth authProfile;
    private RecyclerView recyclerView;
    private budgetAdapter adapter;
    private DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

<<<<<<< Updated upstream
=======
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<>(DashboardActivity.this, android.R.layout.simple_list_item_1, myArrayList);

        myListView = (ListView) findViewById(R.id.listview1);
        myListView.setAdapter(myArrayAdapter);

        mRef = FirebaseDatabase.getInstance().getReference();

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(String.class);
                myArrayList.add(value);
                myArrayAdapter.notifyDataSetChanged();
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


>>>>>>> Stashed changes
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Dashboard");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

<<<<<<< Updated upstream
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
=======
        // Initialize the swipeContainer

>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
=======
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

        } else if (id == R.id.add_expense) {
            Intent intent = new Intent(DashboardActivity.this, AddExpenseActivity.class);
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
>>>>>>> Stashed changes
    }
}


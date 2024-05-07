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

public class MainDashboard extends AppCompatActivity {
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);
        //para sa title
        //getSupportActionBar().setTitle("Export");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Budget Tracker Software");
        }

        Button btnGoals = findViewById(R.id.btn_expenses);
        Button btnExpense = findViewById(R.id.btn_goals);
        btnGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainDashboard.this, GoalActivity.class);
                startActivity(intent);
            }
        });
        btnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainDashboard.this, AddExpenseActivity.class);
                startActivity(intent);
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
            NavUtils.navigateUpFromSameTask(MainDashboard.this);
            finish();
        } else if (id == R.id.menu_refresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        } else if (id == R.id.menu_update_profile) {
            Intent intent = new Intent (MainDashboard.this, UpdateProfile.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_profile) {
            Intent intent = new Intent (MainDashboard.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_update_email) {
            Intent intent = new Intent (MainDashboard.this, UpdateEmail.class);
            startActivity(intent);
        } else if (id == R.id.menu_change_pass) {
            Intent intent = new Intent (MainDashboard.this, ChangePassword.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_delete) {
            Intent intent = new Intent (MainDashboard.this, DeleteUser.class);
            startActivity(intent);
        } else if (id == R.id.menu_home) {
            Intent intent = new Intent(MainDashboard.this, DashboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_goals) {
            Intent intent = new Intent(MainDashboard.this, GoalActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_export) {
            Intent intent = new Intent(MainDashboard.this, ExportActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(MainDashboard.this, "Logged Out", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainDashboard.this, MainActivity.class);

            //clear stack instance & close activity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(MainDashboard.this, "Something went wrong!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
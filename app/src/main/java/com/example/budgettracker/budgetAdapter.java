package com.example.budgettracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class budgetAdapter extends FirebaseRecyclerAdapter<
        BudgetHistory, budgetAdapter.budgetsViewholder> {

    public budgetAdapter(
            @NonNull FirebaseRecyclerOptions<BudgetHistory> options)
    {
        super(options);
    }

    @Override
    protected void
    onBindViewHolder(@NonNull budgetsViewholder holder,
                     int position, @NonNull BudgetHistory model)
    {

        // Add amount from model class (here
        // "BudgetHistory.class")to appropriate view in Card
        // view (here "componenthistory.xml")
        holder.amount.setText(model.getAmount());

        // Add category from model class (here
        // "BudgetHistory.class")to appropriate view in Card
        // view (here "componenthistory.xml")
        holder.category.setText(model.getCategory());

        // Add date from model class (here
        // "BudgetHistory.class")to appropriate view in Card
        // view (here "componenthistory.xml")
        holder.date.setText(model.getDate());

        // Add note from model class (here
        // "BudgetHistory.class")to appropriate view in Card
        // view (here "componenthistory.xml")
        holder.note.setText(model.getNote());
    }

    @NonNull
    @Override
    public budgetsViewholder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.componenthistory, parent, false);
        return new budgetsViewholder(view);
    }

    static class budgetsViewholder extends RecyclerView.ViewHolder {
        TextView amount, category, date, note;

        public budgetsViewholder(@NonNull View itemView)
        {
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            category = itemView.findViewById(R.id.textView2);
            date = itemView.findViewById(R.id.date);
            note = itemView.findViewById(R.id.note);
        }
    }
}



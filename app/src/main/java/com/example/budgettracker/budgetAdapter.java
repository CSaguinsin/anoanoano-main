package com.example.budgettracker;

<<<<<<< Updated upstream
=======
import android.content.Context;
>>>>>>> Stashed changes
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

<<<<<<< Updated upstream
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
=======
import java.util.List;

public class budgetAdapter extends RecyclerView.Adapter<budgetAdapter.budgetsViewholder> {

    private final List<Expense> data;

    public budgetAdapter(Context context, List<Expense> data) {
        this.data  = data;
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

    @Override
    public void onBindViewHolder(@NonNull budgetsViewholder holder, int position) {
        Expense model = data.get(position);
>>>>>>> Stashed changes

        // Add amount from model class (here
        // "BudgetHistory.class")to appropriate view in Card
        // view (here "componenthistory.xml")
<<<<<<< Updated upstream
        holder.amount.setText(model.getAmount());
=======
        holder.amount.setText(String.valueOf(model.getAmount()));
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
    @NonNull
    @Override
    public budgetsViewholder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.componenthistory, parent, false);
        return new budgetsViewholder(view);
=======
    @Override
    public int getItemCount() {
        return 0;
>>>>>>> Stashed changes
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



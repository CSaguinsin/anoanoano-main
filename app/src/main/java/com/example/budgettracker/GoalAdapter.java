package com.example.budgettracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ViewHolder> {
    private List<Goal> goalsList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView goalNameTextView;
        public TextView targetAmountTextView;
        public TextView dateTextView;
        public TextView categoryTextView;
        public TextView progressTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            goalNameTextView = itemView.findViewById(R.id.goalNameTextView);
            targetAmountTextView = itemView.findViewById(R.id.targetAmountTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            progressTextView = itemView.findViewById(R.id.progressTextView);
        }
    }

    public GoalAdapter(List<Goal> goalsList) {
        this.goalsList = goalsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goal goal = goalsList.get(position);
        holder.goalNameTextView.setText(goal.getGoalName());
        holder.targetAmountTextView.setText(String.format("Target Amount: PHP %.2f", goal.getTargetAmount()));
        holder.dateTextView.setText("Date: " + goal.getSelectedDate());
        holder.categoryTextView.setText(goal.getCategory());
        holder.progressTextView.setText("Progress: " + goal.getCurrentAmount() + "/" + goal.getTargetAmount() + " (" + String.format("%.2f%%", goal.getProgress())+")");
    }

    @Override
    public int getItemCount() {
        return goalsList.size();
    }
}

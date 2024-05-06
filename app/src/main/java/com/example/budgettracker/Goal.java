package com.example.budgettracker;

public class Goal {
    private String goalName;
    private double targetAmount;
    private double currentAmount;
    private String selectedDate;
    private String category;
    private double progress;

    public Goal() {
        // Default constructor required for calls to DataSnapshot.getValue(Goal.class)
    }

    public Goal(String goalName, double targetAmount, String selectedDate, String category) {
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.selectedDate = selectedDate;
        this.category = category;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }
    public double getCurrentAmount() {return currentAmount; }
    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }
    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public double getProgress() {
        return getCurrentAmount()/getTargetAmount();
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

}

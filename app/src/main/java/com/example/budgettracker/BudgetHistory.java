package com.example.budgettracker;

public class BudgetHistory {

    // Variable to store data corresponding
    // to firstname keyword in database
    private String amount;

    // Variable to store data corresponding
    // to lastname keyword in database
    private String category;

    // Variable to store data corresponding
    // to age keyword in database
    private String date;

    // Variable to store data corresponding
    // to age keyword in database
    private String note;

    // Mandatory empty constructor
    // for use of FirebaseUI


    // Getter and setter method
    public String getAmount()
    {
        return amount;
    }
    public void setAmount(String amount)
    {
        this.amount = amount;
    }
    public String getCategory()
    {
        return category;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }
    public String getDate()
    {
        return date;
    }
    public void setDate(String date)
    {
        this.date = date;
    }

    public String getNote()
    {
        return note;
    }
    public void setNote(String note)
    {
        this.note = note;
    }
}

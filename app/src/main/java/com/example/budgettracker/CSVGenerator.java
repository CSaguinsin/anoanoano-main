package com.example.budgettracker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class CSVGenerator {

    public static void generateExpenseCSV(Context context, List<Expense> expenses) {
        generateCSV(context, expenses, "expenses.csv", "Amount,Category,Note,Date\n");
    }

    public static void generateGoalCSV(Context context, List<Goal> goals) {
        generateCSV(context, goals, "goals.csv", "Goal Name,Target Amount,Selected Date,Category\n");
    }

    private static void generateCSV(Context context, List<?> data, String defaultFileName, String header) {
        // Check if external storage is available
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Log.e("CSVGenerator", "External storage not available");
            return;
        }

        // Get the Downloads directory
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // Create the CSV file
        File csvFile = new File(downloadsDir, defaultFileName);

        try {
            // Create file output stream and writer
            FileOutputStream fileOutputStream = new FileOutputStream(csvFile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            // Write header to the file
            outputStreamWriter.write(header);

            // Write CSV data to the file
            for (Object obj : data) {
                if (obj instanceof Expense) {
                    Expense expense = (Expense) obj;
                    outputStreamWriter.write(expense.getAmount() + "," + expense.getCategory() + "," + expense.getNote() + "," + expense.getDate() + "\n");
                } else if (obj instanceof Goal) {
                    Goal goal = (Goal) obj;
                    outputStreamWriter.write(goal.getGoalName() + "," + goal.getTargetAmount() + "," + goal.getSelectedDate() + "," + goal.getCategory() + "\n");
                }
            }

            // Close the writer and output stream
            outputStreamWriter.close();
            fileOutputStream.close();

            Log.d("CSVGenerator", "CSV file saved: " + csvFile.getAbsolutePath());

            // Open the CSV file
            openCSVFile(context, csvFile);
        } catch (IOException e) {
            Log.e("CSVGenerator", "Error saving CSV file: " + e.getMessage());
        }
    }

    private static void openCSVFile(Context context, File file) {
        try {
            Uri fileUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            } else {
                fileUri = Uri.fromFile(file);
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "text/csv");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("CSVGenerator", "Error opening CSV file: " + e.getMessage());
        }
    }
}

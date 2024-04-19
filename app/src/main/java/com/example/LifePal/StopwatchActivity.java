package com.example.LifePal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class StopwatchActivity extends AppCompatActivity {
    private TextView timerTextView, title;

    private TextView stopwatchCurrentlyPause;
    private ImageButton startPauseButton;
    private FirebaseFirestore db;
    private TextView descriptionView;
    private ImageButton endButton;
    private ImageButton returnButton;

    private Handler handler = new Handler();
    int hours, minutes, secs, fullSeconds;
    private boolean isRunning;

    private boolean sessionFinished;

    private Context cntx;
    private SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        cntx = getApplicationContext();
        myPrefs = cntx.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);

        db = FirebaseFirestore.getInstance();

        // Binding UI components
        timerTextView = findViewById(R.id.stopwatchTextView);
        startPauseButton = findViewById(R.id.stopwatchstartPauseButton);
        startPauseButton.setImageResource(R.drawable.resumebutton);
        endButton = findViewById(R.id.stopwatchEnd);
        returnButton = findViewById(R.id.stopwatchReturn);
        descriptionView = findViewById(R.id.DescriptionStopwatch);
        String description = myPrefs.getString("description","");
        descriptionView.setText(description);
        title = findViewById(R.id.title);
        stopwatchCurrentlyPause = findViewById(R.id.CurrentlyPausedStopwatch);
        stopwatchCurrentlyPause.setVisibility(View.INVISIBLE);
        String titleString = myPrefs.getString("title","");
        title.setText(titleString);
        stopwatchCurrentlyPause.setVisibility(View.VISIBLE);
        fullSeconds = myPrefs.getInt("seconds",0);

        int hour = fullSeconds / 3600;
        int minute = (fullSeconds % 3600) / 60;
        int second = fullSeconds % 60;
        timerTextView.setText(String.format("%02d:%02d:%02d", hour, minute, second));

//        startStopwatch();
//        isRunning = true;
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    stopwatchCurrentlyPause.setVisibility(View.VISIBLE);
                    pauseStopwatch();
                } else {
                    stopwatchCurrentlyPause.setVisibility(View.INVISIBLE);
                    startStopwatch();
                }
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitConfirmationDialog();
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("tasks").document(titleString)
                        .update(
                                "timeSpent",Integer.toString(fullSeconds)
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                String title = myPrefs.getString("title","");
                                Task currentTask = MainActivity.tasks.get(MainActivity.taskAdapter.findTask(title));
                                currentTask.setTimeSpent(Integer.toString(fullSeconds));

                                MainActivity.taskAdapter.notifyDataSetChanged();
                                Intent intent = new Intent(StopwatchActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(e -> Toast.makeText(cntx, "Failed to exit", Toast.LENGTH_LONG).show());


            }
        });


        runStopwatch();
    }




    private void showExitConfirmationDialog() {
        // Build and show the dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Exit Event")
                .setMessage("Do you want to exit this event?")
                .setPositiveButton(android.R.string.yes, null)
                .setNegativeButton(android.R.string.no, null)
                .show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        positiveButton.setTextColor(getResources().getColor(R.color.black));
        negativeButton.setTextColor(getResources().getColor(R.color.black));

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User confirmed to exit, navigate to MainActivity
                String title = myPrefs.getString("title","");
                db.collection("tasks").document(title)
                        .update(
                                "timeSpent",Integer.toString(fullSeconds),
                                "finished",true
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                sessionFinished = true;
//                                myPrefs = cntx.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
//                                String title = myPrefs.getString("title","");
//                                Task currentTask = MainActivity.completedTasks.get(MainActivity.completedTaskAdapter.findTask(title));
//                                currentTask.setTimeSpent(Integer.toString(fullSeconds));
//                                currentTask.setFinished(true);
//                                MainActivity.taskAdapter.notifyDataSetChanged();
//                                MainActivity.completedTaskAdapter.notifyDataSetChanged();

                                Intent intent = new Intent(StopwatchActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(e -> Toast.makeText(cntx, "Failed to exit", Toast.LENGTH_LONG).show());



                dialog.dismiss();
            }
        });
    }


    private void startStopwatch() {
        isRunning = true;
        startPauseButton.setImageResource(R.drawable.pausebutton);
    }

    private void pauseStopwatch(){
        isRunning = false;
        startPauseButton.setImageResource(R.drawable.resumebutton);
    }

    private void runStopwatch(){
        final Handler handler = new Handler();


        handler.post(new Runnable() {@Override

        public void run() {
            hours = fullSeconds / 3600;
            minutes = (fullSeconds % 3600) / 60;
            secs = fullSeconds % 60;

            // if running increment the seconds
            if (isRunning) {
                String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);

                timerTextView.setText(time);

                fullSeconds++;
            }
            handler.postDelayed(this, 1000);
        }
        });

    }


}

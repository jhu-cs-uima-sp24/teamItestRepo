package com.example.LifePal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

public class TimerActivity extends AppCompatActivity {

    private TextView timerTextView, title;

    private TextView currentlyPause;
    private ImageButton startPauseButton;

    private ImageButton endEventButton;

    private ImageButton returnEvent;
    private TextView descriptionView;
    private TextView timePastView;
    private Button backHome;
    private int hours, minutes, secs;

    private int secondPast;
    private boolean isRunning;

    private boolean sessionFinished;
    private FirebaseFirestore db;

    private int index;

    private Context cntx;
    private SharedPreferences myPrefs;
    private String timePast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_timer);
        cntx = getApplicationContext();
        myPrefs = cntx.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        String titleString = myPrefs.getString("title","");
        descriptionView = findViewById(R.id.timerDescription);
        String description = myPrefs.getString("description","");
        descriptionView.setText(description);
        String time;
        timePast = "Time Past: ";
        int fullSecond;
        fullSecond = myPrefs.getInt("seconds",0);

            hours = fullSecond / 3600;
            minutes = (fullSecond % 3600) / 60;
            secs = fullSecond % 60;
            time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);
            //remove from shared preferences
            SharedPreferences.Editor peditor = myPrefs.edit();
            peditor.remove("seconds");



        // Binding UI components
        timePastView = findViewById(R.id.amountPast);
        timerTextView = findViewById(R.id.timerTextView);
        timerTextView.setText(time);
        startPauseButton = findViewById(R.id.timerstartPauseButton);
        startPauseButton.setImageResource(R.drawable.resumebutton);
        endEventButton = findViewById(R.id.timerEnd);
        returnEvent = findViewById(R.id.timerReturn);
        backHome = findViewById(R.id.backMainButton);
        title = findViewById(R.id.title);
        title.setText(titleString);
        currentlyPause = findViewById(R.id.CurrentlyPaused);



        backHome.setVisibility(View.INVISIBLE);
        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = myPrefs.getString("title","");
                db.collection("tasks").document(title)
                        .update(
                                "timeLeft",Integer.toString(0),
                                "timeSpent",Integer.toString(secondPast),
                                "finished",true
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                myPrefs = cntx.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
                                String title = myPrefs.getString("title","");
                                Task currentTask = MainActivity.tasks.get(MainActivity.taskAdapter.findTask(title));
                                Task newTask = new Task(currentTask.getTaskName(),currentTask.getDescription(),Integer.toString(secondPast),currentTask.getTag(), currentTask.getIsStopWatch());

                                newTask.setFinished(true);
                                MainActivity.completedTasks.add(newTask);
                                MainActivity.tasks.remove(MainActivity.taskAdapter.findTask(title));
                                MainActivity.taskAdapter.notifyDataSetChanged();
                                MainActivity.completedTaskAdapter.notifyDataSetChanged();

                                Intent intent = new Intent(TimerActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(e -> Toast.makeText(cntx, "Failed to exit", Toast.LENGTH_LONG).show());


            }
        });



        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    currentlyPause.setVisibility(View.VISIBLE);
                    pauseTimer();
                } else {
                    currentlyPause.setVisibility(View.INVISIBLE);
                    startTimer();
                }
            }
        });

        endEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitConfirmationDialog();
            }
        });

        returnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // user quits to main, task doesn't end
                String title = myPrefs.getString("title","");
                Task currentTask = MainActivity.tasks.get(MainActivity.taskAdapter.findTask(title));
                int remaining_time = Integer.parseInt(currentTask.getTimeLeft())-secondPast;
                db.collection("tasks").document(titleString)
                        .update(
                                "timeLeft",Integer.toString(remaining_time),
                                "timeSpent",Integer.toString(secondPast)
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                currentTask.setTimeLeft(Integer.toString(remaining_time));
                                currentTask.setTimeSpent(Integer.toString(secondPast));
                                MainActivity.taskAdapter.notifyDataSetChanged();
                                Intent intent = new Intent(TimerActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(e -> Toast.makeText(cntx, "Failed to exit", Toast.LENGTH_LONG).show());


            }
        });

        runTimer(fullSecond);
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
            public void onClick(View v) { // user ends event, event is put in to do list
                // User confirmed to exit, navigate to MainActivity
                sessionFinished = true;

                myPrefs = cntx.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
                String title = myPrefs.getString("title","");

                db.collection("tasks").document(title)
                        .update(
                                "timeLeft",Integer.toString(0),
                                "timeSpent",Integer.toString(secondPast),
                                "finished",true
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                MainActivity.taskAdapter.notifyDataSetChanged();
//                                MainActivity.completedTaskAdapter.notifyDataSetChanged();
                                Intent intent = new Intent(TimerActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(e -> Toast.makeText(cntx, "Failed to exit", Toast.LENGTH_LONG).show());
                dialog.dismiss();
            }
        });
    }


    private void startTimer() {
        isRunning = true;
        startPauseButton.setImageResource(R.drawable.pausebutton);
    }

    private void pauseTimer(){
        isRunning = false;
        startPauseButton.setImageResource(R.drawable.resumebutton);
    }


    private void runTimer(int fullSecond){
        final Handler handler = new Handler();

        final int[] fullSecondArray = new int[]{fullSecond};
        handler.post(new Runnable() {@Override

        public void run() {
            hours = fullSecondArray[0] / 3600;
            minutes = (fullSecondArray[0] % 3600) / 60;
            secs = fullSecondArray[0] % 60;

            // if running increment the seconds
            if (isRunning) {
                String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);
                String amountPast = String.format(Locale.getDefault(), "%02d:%02d:%02d", secondPast / 3600, (secondPast % 3600) / 60, secondPast % 60);

                String finalTime = timePast + amountPast;
                timePastView.setText(finalTime);
                timerTextView.setText(time);

                fullSecondArray[0]--;
                secondPast++;

                if(fullSecondArray[0] < 0){
                    isRunning = false;
                    sessionFinished = true;
                    startPauseButton.setVisibility(View.GONE);
                    endEventButton.setVisibility(View.GONE);
                    returnEvent.setVisibility(View.GONE);
                    backHome.setVisibility(View.VISIBLE);
                    currentlyPause.setVisibility(View.VISIBLE);
                    currentlyPause.setText("Session Complete");

                }
            }
            handler.postDelayed(this, 1000);
        }
        });

    }




}

package com.example.a5_sample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    private TextView timerTextView, title;

    private TextView currentlyPause;
    private ImageButton startPauseButton;

    private ImageButton endEventButton;

    private ImageButton returnEvent;
    private Button backHome;
    private int hours, minutes, secs;

    private int secondPast;
    private boolean isRunning;

    private boolean sessionFinished;


    private Context cntx;
    private SharedPreferences myPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        cntx = getApplicationContext();
        myPrefs = cntx.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        String titleString = myPrefs.getString("title","");
        int fullSecond = myPrefs.getInt("seconds",0);
        hours = fullSecond / 3600;
        minutes = (fullSecond % 3600) / 60;
        secs = fullSecond % 60;
        String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);



        // Binding UI components
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
                myPrefs = cntx.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
                String title = myPrefs.getString("title","");
                Task currentTask = MainActivity.tasks.get(MainActivity.taskAdapter.findTask(title));
                Task newTask = new Task(currentTask.getTaskName(),currentTask.getDescription(),secondPast,currentTask.getTag(), currentTask.getIsStopWatch());
                newTask.setFinished(true);
                MainActivity.completedTasks.add(newTask);
                MainActivity.tasks.remove(MainActivity.taskAdapter.findTask(title));
                MainActivity.taskAdapter.notifyDataSetChanged();
                MainActivity.completedTaskAdapter.notifyDataSetChanged();

                Intent intent = new Intent(TimerActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
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
                currentTask.setTimeLeft(currentTask.getTimeLeft()-secondPast);
                currentTask.setTimeSpent(secondPast);
                MainActivity.taskAdapter.notifyDataSetChanged();
                Intent intent = new Intent(TimerActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
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
                Task currentTask = MainActivity.tasks.get(MainActivity.taskAdapter.findTask(title));
                Task newTask = new Task(currentTask.getTaskName(),currentTask.getDescription(),secondPast,currentTask.getTag(), currentTask.getIsStopWatch());
                newTask.setFinished(true);
                MainActivity.completedTasks.add(newTask);
                MainActivity.tasks.remove(MainActivity.taskAdapter.findTask(title));
                MainActivity.taskAdapter.notifyDataSetChanged();
                MainActivity.completedTaskAdapter.notifyDataSetChanged();

                Intent intent = new Intent(TimerActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

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

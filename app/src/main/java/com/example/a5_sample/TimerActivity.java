package com.example.a5_sample;

import android.content.DialogInterface;
import android.content.Intent;
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

    private TextView timerTextView;
    private ImageButton startPauseButton;

    private ImageButton endEventButton;

    private ImageButton returnEvent;
    private int hours, minutes, secs;
    private int fullSecond = 3600;

    private int secondPast;
    private boolean isRunning;

    private boolean sessionFinished;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        hours = fullSecond / 3600;
        minutes = (fullSecond % 3600) / 60;
        secs = fullSecond % 60;
        String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);



        // Binding UI components
        timerTextView = findViewById(R.id.timerTextView);
        timerTextView.setText(time);
        startPauseButton = findViewById(R.id.timerstartPauseButton);
        endEventButton = findViewById(R.id.timerEnd);
        returnEvent = findViewById(R.id.timerReturn);
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    pauseTimer();
                } else {
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
            public void onClick(View view) {
                navigateToMainActivity();
            }
        });

        runTimer();
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
                sessionFinished = true;
                navigateToMainActivity();
                dialog.dismiss();
            }
        });
    }


    private void navigateToMainActivity() {
        Intent intent = new Intent(TimerActivity.this, MainActivity.class);
        intent.putExtra("secondPast", secondPast);
        intent.putExtra("IsSessionFinished", sessionFinished);
        startActivity(intent);
    }

    private void startTimer() {
        isRunning = true;
        startPauseButton.setImageResource(R.drawable.pause_button);
    }

    private void pauseTimer(){
        isRunning = false;
        startPauseButton.setImageResource(R.drawable.chat_icon);
    }


    private void runTimer(){
        final Handler handler = new Handler();

        handler.post(new Runnable() {@Override

        public void run() {
            hours = fullSecond / 3600;
            minutes = (fullSecond % 3600) / 60;
            secs = fullSecond % 60;

            // if running increment the seconds
            if (isRunning) {
                String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);

                timerTextView.setText(time);

                fullSecond--;
                secondPast++;
            }
            handler.postDelayed(this, 1000);
        }
        });

    }




}

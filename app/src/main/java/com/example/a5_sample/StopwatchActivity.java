package com.example.a5_sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class StopwatchActivity extends AppCompatActivity {
    private TextView timerTextView;
    private ImageButton startPauseButton;
    private ImageButton endButton;
    private ImageButton returnButton;
    private Handler handler = new Handler();
    int hours, minutes, secs, fullSeconds;
    private boolean isRunning;

    private boolean sessionFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        // Binding UI components
        timerTextView = findViewById(R.id.stopwatchTextView);
        startPauseButton = findViewById(R.id.stopwatchstartPauseButton);
        endButton = findViewById(R.id.stopwatchEnd);
        returnButton = findViewById(R.id.stopwatchReturn);

        startStopwatch();
        isRunning = true;
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    pauseStopwatch();
                } else {
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
                navigateToMainActivity();
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
                sessionFinished = true;
                navigateToMainActivity();
                dialog.dismiss();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(StopwatchActivity.this, MainActivity.class);
        intent.putExtra("secondPast", fullSeconds);
        intent.putExtra("sessionFinished", sessionFinished);
        startActivity(intent);
    }


    private void startStopwatch() {
        isRunning = true;
        startPauseButton.setImageResource(R.drawable.pause_button);
    }

    private void pauseStopwatch(){
        isRunning = false;
        startPauseButton.setImageResource(R.drawable.chat_icon);
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

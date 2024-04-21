package com.example.LifePal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.Map;

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
    private String username;
    private double prev_points;
    private int points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        cntx = getApplicationContext();
        myPrefs = cntx.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        username = myPrefs.getString("username","");
        db = FirebaseFirestore.getInstance();
        readUserDataFromFirebase();
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
        prev_points = fullSeconds;

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
                db.collection("users").document(username).collection("tasks").document(titleString)
                        .update(
                                "timeSpent",Integer.toString(fullSeconds)
                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                String title = myPrefs.getString("title","");
                                Task currentTask = MainActivity.tasks.get(MainActivity.taskAdapter.findTask(title));
                                currentTask.setTimeSpent(Integer.toString(fullSeconds));

                                MainActivity.taskAdapter.notifyDataSetChanged();
                                points += (int) (fullSeconds - prev_points * 10 / 15);
                                updateCurrentPointsInFirebase(points);
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
                String username = myPrefs.getString("username","");
                db.collection("users").document(username).collection("tasks").document(title)
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
                                points += (int) (fullSeconds - prev_points * 10 / 15);
                                updateCurrentPointsInFirebase(points);

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

                if (fullSeconds % 60 == 0) {
                    show_toast();
                }
            }
            handler.postDelayed(this, 1000);
        }
        });

    }
    private void show_toast() {
        int points = (int) (60 * 10 / 15);
        int pet_user = myPrefs.getInt("pet_id", -1);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.toast_text);
        ImageView icon = (ImageView) layout.findViewById(R.id.toast_icon);
        text.setText("Current Session Points +" + points + "pts");
        Drawable drawable = ContextCompat.getDrawable(this, pet_user);
        if (drawable != null) {
            drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), 64, 64, true));
            icon.setImageDrawable(drawable);
        } else {
            Log.e("ToastError", "Drawable resource for pet_user not found.");
            icon.setVisibility(View.GONE);
        }

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
//        Toast toast = Toast.makeText(StopwatchActivity.this, "Current Session Points    +" + String.valueOf(points) + "pts", Toast.LENGTH_SHORT);
//        View toastView = toast.getView();
//        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
//        toastMessage.setTextSize(15);
//        toastMessage.setTypeface(Typeface.DEFAULT);
//        toastMessage.setTextColor(getResources().getColor(R.color.dark_gray));
//
//        int paddingLeft = 16;
//        int paddingRight = 16;
//        toastMessage.setPadding(paddingLeft, 0, paddingRight, 0);
//
//        Drawable drawable = ContextCompat.getDrawable(StopwatchActivity.this, pet_user);
//        int desiredWidth = 64;
//        int desiredHeight = 64;
//        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//        Drawable resizedDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, desiredWidth, desiredHeight, true));
//        int drawablePadding = 16;
//        resizedDrawable.setBounds(0, 0, desiredWidth, desiredHeight);
//
//        toastMessage.setCompoundDrawables(resizedDrawable, null, null, null);
//        toastMessage.setGravity(Gravity.CENTER);
//        toastMessage.setCompoundDrawablePadding(30);
//        toastView.setBackgroundResource(R.drawable.outlined_rounded_rect);
//        toast.show();
    }

    private void readUserDataFromFirebase() {
        db.collection("users").document(username)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> userData = documentSnapshot.getData();
                            if (userData != null) {
                                if (userData.containsKey("current_points")) {
                                    points = Math.toIntExact((Long) userData.get("pet_id"));
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                    }
                });
    }

    private void updateCurrentPointsInFirebase(int newCurrentPoints) {
        db.collection("users").document(username)
                .update("current_points", newCurrentPoints)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Current points updated successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update current points
                    }
                });
    }


}

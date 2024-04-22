package com.example.LifePal;
import com.example.LifePal.ui.stats.GridSpacingItemDecoration;
import com.example.LifePal.ui.stats.TagAdapter;
import com.example.LifePal.ui.stats.TagModel;
import com.example.LifePal.ui.stats.VerticalSpaceItemDecoration;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class CustomPieChart extends View {
    private static final String TAG = "";
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float[] slicePercentages = {60, 20, 10, 10};
    // Example percentages
    int breakPercentage = 0;
    int studyPercentage = 0;
    int workoutPercentage = 0;
    int gamingPercentage = 0;
    private int[] colors = {0xFFFCB2DA, 0xFFBCF1D1, 0xFFC0D6F9, 0xFFF8DE9C}; // Red, Green, Blue, Yellow
    //#FCB2DA

    public CustomPieChart(Context context) {
        super(context);
    }

    public CustomPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPieChart(canvas);
    }

    private void drawPieChart(Canvas canvas) {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("statsMode", Context.MODE_PRIVATE);
        String mode = sharedPreferences.getString("mode", "day");

        Date startDate = new Date(
                sharedPreferences.getInt("yearVal", 0) - 1900, // Adjusting year as Date constructor expects year from 1900
                sharedPreferences.getInt("monthVal", 0) - 1, // Adjusting month as Date constructor expects 0-11
                sharedPreferences.getInt("dayVal", 0)
        );

        // Initialize a Calendar object from startDate
        Calendar endDate2 = Calendar.getInstance();
        Calendar startDate2 = Calendar.getInstance();
        endDate2.setTime(startDate);

        // Determine endDate based on the mode
        switch (mode) {
            case "day":
                endDate2.add(Calendar.DAY_OF_MONTH, 1); // Add one day
                break;
            case "week":
                endDate2.add(Calendar.WEEK_OF_YEAR, 1); // Add one week
                break;
            case "month":
                endDate2.add(Calendar.MONTH, 1); // Add one month
                break;
            default:
                break;
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences1 = getContext().getSharedPreferences("t", Context.MODE_PRIVATE);
        String user = sharedPreferences1.getString("user_name", "default");


        db.collection("users").document(user).collection("tasks")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent( QuerySnapshot snapshots,
                                         FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        for (DocumentSnapshot doc : snapshots.getDocuments()) {

                            if (doc.exists()) {
                                int totalTime = 0;
                                int day = doc.getLong("Day").intValue();
                                int month = doc.getLong("Month").intValue();
                                int year = doc.getLong("Year").intValue();
                                int hour = doc.getLong("Hour").intValue();
                                int minute = doc.getLong("Minute").intValue();
                                int second = doc.getLong("Second").intValue();

                                Date thisDate = new Date(year - 1900, month - 1, day);
                                Calendar thisDateCal = Calendar.getInstance();
                                thisDateCal.setTime(thisDate);

                                if (isDateBetween(startDate2, endDate2, thisDateCal)) {

                                    String tag = doc.getString("tag");
                                    if (tag.equals("break")) {
                                        breakPercentage += doc.getLong("timSpent").intValue();
                                    } else if (tag.equals("study")) {
                                        studyPercentage += doc.getLong("timSpent").intValue();
                                    } else if (tag.equals("workout")) {
                                        workoutPercentage += doc.getLong("timSpent").intValue();
                                    } else if (tag.equals("gaming")) {
                                        gamingPercentage += doc.getLong("timSpent").intValue();
                                    }

                                } else {
                                    Log.d(TAG, "Current data: null");
                                }
                            }
                        }
                    }
                });




















        db.collection("tags").document("break")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot,
                                        FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG1", "Listen failed.", e);
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            int totalTime = 0;
                            Map<String, Object> entry = documentSnapshot.getData();
                            if (entry != null) {
                                for (String s : entry.keySet()) {
                                    int firstComma = s.indexOf(",");
                                    int secondComma = s.indexOf(",", firstComma + 1);
                                    int thirdComma = s.lastIndexOf(",");
                                    int day = Integer.parseInt(s.substring(0, firstComma));
                                    int month = Integer.parseInt(s.substring(firstComma + 1, secondComma));
                                    int year = Integer.parseInt(s.substring(secondComma + 1, thirdComma));

                                    Date thisDate = new Date(year - 1900, month - 1, day);
                                    Calendar thisDateCal = Calendar.getInstance();
                                    thisDateCal.setTime(thisDate);

                                    if (isDateBetween(startDate2, endDate2, thisDateCal)) {
                                        Object value = entry.get(s);
                                        if (value instanceof Long) {
                                            totalTime += ((Long) value).intValue();
                                        } else if (value instanceof Double) {
                                            totalTime += ((Double) value).intValue();
                                        }
                                    }
                                }
                                breakPercentage = totalTime;
                                Log.w("TAG1", "Break: " + breakPercentage);
                            }
                        } else {
                            Log.d("TAG1", "Current data: null");
                        }
                    }
                });



        db.collection("tags").document("study")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot,
                                        FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG1", "Listen failed.", e);
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            int totalTime = 0;
                            Map<String, Object> entry = documentSnapshot.getData();
                            if (entry != null) {
                                for (String s : entry.keySet()) {
                                    int firstComma = s.indexOf(",");
                                    int secondComma = s.indexOf(",", firstComma + 1);
                                    int thirdComma = s.lastIndexOf(",");
                                    int day = Integer.parseInt(s.substring(0, firstComma));
                                    int month = Integer.parseInt(s.substring(firstComma + 1, secondComma));
                                    int year = Integer.parseInt(s.substring(secondComma + 1, thirdComma));

                                    Date thisDate = new Date(year - 1900, month - 1, day);
                                    Calendar thisDateCal = Calendar.getInstance();
                                    thisDateCal.setTime(thisDate);

                                    if (isDateBetween(startDate2, endDate2, thisDateCal)) {
                                        Object value = entry.get(s);
                                        if (value instanceof Long) {
                                            totalTime += ((Long) value).intValue();
                                        } else if (value instanceof Double) {
                                            totalTime += ((Double) value).intValue();
                                        }
                                    }
                                }
                                studyPercentage = totalTime;
                                Log.w("TAG1", "" +
                                        "Study: " + studyPercentage);
                            }
                        } else {
                            Log.d("TAG1", "Current data: null");
                        }
                    }
                });


        db.collection("tags").document("gaming")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot,
                                        FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG1", "Listen failed.", e);
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            int totalTime = 0;
                            Map<String, Object> entry = documentSnapshot.getData();
                            if (entry != null) {
                                for (String s : entry.keySet()) {
                                    int firstComma = s.indexOf(",");
                                    int secondComma = s.indexOf(",", firstComma + 1);
                                    int thirdComma = s.lastIndexOf(",");
                                    int day = Integer.parseInt(s.substring(0, firstComma));
                                    int month = Integer.parseInt(s.substring(firstComma + 1, secondComma));
                                    int year = Integer.parseInt(s.substring(secondComma + 1, thirdComma));

                                    Date thisDate = new Date(year - 1900, month - 1, day);
                                    Calendar thisDateCal = Calendar.getInstance();
                                    thisDateCal.setTime(thisDate);

                                    if (isDateBetween(startDate2, endDate2, thisDateCal)) {
                                        Object value = entry.get(s);
                                        if (value instanceof Long) {
                                            totalTime += ((Long) value).intValue();
                                        } else if (value instanceof Double) {
                                            totalTime += ((Double) value).intValue();
                                        }
                                    }
                                }
                                gamingPercentage = totalTime;
                                Log.w("TAG1", "Gaming: " + gamingPercentage);
                            }
                        } else {
                            Log.d("TAG1", "Current data: null");
                        }
                    }
                });

        db.collection("tags").document("workout")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot,
                                        FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG1", "Listen failed.", e);
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            int totalTime = 0;
                            Map<String, Object> entry = documentSnapshot.getData();
                            if (entry != null) {
                                for (String s : entry.keySet()) {
                                    int firstComma = s.indexOf(",");
                                    int secondComma = s.indexOf(",", firstComma + 1);
                                    int thirdComma = s.lastIndexOf(",");
                                    int day = Integer.parseInt(s.substring(0, firstComma));
                                    int month = Integer.parseInt(s.substring(firstComma + 1, secondComma));
                                    int year = Integer.parseInt(s.substring(secondComma + 1, thirdComma));

                                    Date thisDate = new Date(year - 1900, month - 1, day);
                                    Calendar thisDateCal = Calendar.getInstance();
                                    thisDateCal.setTime(thisDate);

                                    if (isDateBetween(startDate2, endDate2, thisDateCal)) {
                                        Object value = entry.get(s);
                                        if (value instanceof Long) {
                                            totalTime += ((Long) value).intValue();
                                        } else if (value instanceof Double) {
                                            totalTime += ((Double) value).intValue();
                                        }
                                    }
                                }
                                workoutPercentage = totalTime;
                                Log.w("TAG1", "Break: " + workoutPercentage);
                            }
                        } else {
                            Log.d("TAG1", "Current data: null");
                        }
                    }
                });


        int total = breakPercentage + studyPercentage + workoutPercentage + gamingPercentage;
        Log.w("TAG", "Total: " + total);
        Log.w("TAG", "Break: " + breakPercentage);
        Log.w("TAG", "Study: " + studyPercentage);
        Log.w("TAG", "Workout: " + workoutPercentage);
        Log.w("TAG", "Gaming: " + gamingPercentage);


        slicePercentages = new float[]{(float) breakPercentage * 100 /total, (float) studyPercentage * 100 /total, (float) workoutPercentage * 100/total, (float) gamingPercentage * 100 /total};


        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height);
        RectF rect = new RectF((float) (size) / 8, 0, (float) (7 * size) / 8, (float) (6 * size) / 8); // Use RectF for the pie chart bounds

        float startAngle = 0;
        for (int i = 0; i < slicePercentages.length; i++) {
            float sweepAngle = (slicePercentages[i] / 100) * 360; // Convert percentage to angle
            paint.setColor(colors[i]);
            canvas.drawArc(rect, startAngle, sweepAngle, true, paint);
            startAngle += sweepAngle;
        }



        }


    public static boolean isDateBetween(Calendar startDate, Calendar endDate, Calendar specificDate) {
        return !specificDate.before(startDate) && !specificDate.after(endDate);
    }

    }

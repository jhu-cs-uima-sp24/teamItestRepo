package com.example.LifePal;
import com.example.LifePal.ui.stats.GridSpacingItemDecoration;
import com.example.LifePal.ui.stats.TagAdapter;
import com.example.LifePal.ui.stats.TagModel;
import com.example.LifePal.ui.stats.VerticalSpaceItemDecoration;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;



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
    int breakPercentage = 60;
    int studyPercentage = 20;
    int workoutPercentage = 10;
    int gamingPercentage = 10;
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

                db.collection("tags").document("break")
                        .get().addOnSuccessListener(documentSnapshot -> {

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

                                    Date thisDate = new Date(year, month, day);
                                    Calendar thisDateCal = Calendar.getInstance();
                                    thisDateCal.setTime(thisDate);

                                    if(isDateBetween(startDate2, endDate2, thisDateCal)){
                                        Object value = entry.get(s);
                                        if (value instanceof Long) {
                                            totalTime += ((Long) value).intValue();
                                        } else if (value instanceof Double) {
                                            totalTime += ((Double) value).intValue();
                                        }
                                    }
                                    breakPercentage = totalTime;
                                }

                            }

                        }).addOnFailureListener(e -> {
                            System.out.println("Error fetching document: " + e.getMessage());
                        });

        db.collection("tags").document("study")
                .get().addOnSuccessListener(documentSnapshot -> {

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

                            Date thisDate = new Date(year, month, day);
                            Calendar thisDateCal = Calendar.getInstance();
                            thisDateCal.setTime(thisDate);

                            if(isDateBetween(startDate2, endDate2, thisDateCal)){
                                Object value = entry.get(s);
                                if (value instanceof Long) {
                                    totalTime += ((Long) value).intValue();
                                } else if (value instanceof Double) {
                                    totalTime += ((Double) value).intValue();
                                }
                            }
                            studyPercentage = totalTime;
                        }

                    }

                }).addOnFailureListener(e -> {
                    System.out.println("Error fetching document: " + e.getMessage());
                });


        db.collection("tags").document("gaming")
                .get().addOnSuccessListener(documentSnapshot -> {

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

                            Date thisDate = new Date(year, month, day);
                            Calendar thisDateCal = Calendar.getInstance();
                            thisDateCal.setTime(thisDate);

                            if(isDateBetween(startDate2, endDate2, thisDateCal)){
                                Object value = entry.get(s);
                                if (value instanceof Long) {
                                    totalTime += ((Long) value).intValue();
                                } else if (value instanceof Double) {
                                    totalTime += ((Double) value).intValue();
                                }
                            }
                            gamingPercentage = totalTime;
                        }

                    }

                }).addOnFailureListener(e -> {
                    System.out.println("Error fetching document: " + e.getMessage());
                });


        db.collection("tags").document("workout")
                .get().addOnSuccessListener(documentSnapshot -> {


                    for(int i = 0; i < 100000; i++);

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

                            Date thisDate = new Date(year, month, day);
                            Calendar thisDateCal = Calendar.getInstance();
                            thisDateCal.setTime(thisDate);

                            if(isDateBetween(startDate2, endDate2, thisDateCal)){
                                Object value = entry.get(s);
                                if (value instanceof Long) {
                                    totalTime += ((Long) value).intValue();
                                } else if (value instanceof Double) {
                                    totalTime += ((Double) value).intValue();
                                }
                            }
                            workoutPercentage = totalTime;


                            int total = breakPercentage + studyPercentage + workoutPercentage + gamingPercentage;


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

                    }

                }).addOnFailureListener(e -> {
                    System.out.println("Error fetching document: " + e.getMessage());
                });


        }


    public static boolean isDateBetween(Calendar startDate, Calendar endDate, Calendar specificDate) {
        return !specificDate.before(startDate) && !specificDate.after(endDate);
    }

    }

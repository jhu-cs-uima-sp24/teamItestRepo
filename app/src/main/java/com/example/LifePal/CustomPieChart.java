package com.example.LifePal;
import com.google.firebase.firestore.FirebaseFirestore;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import android.util.AttributeSet;

public class CustomPieChart extends View {
    private static final String TAG = "";
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float[] slicePercentages = {60, 20, 10, 10}; // Example percentages
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
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        Map<String, Integer> tags = new HashMap<>();
//        tags.put("break", 10);
//        tags.put("study", 10);
//        tags.put("gaming", 20);
//        tags.put("workout", 100);
//
//// Add a new document with a generated ID
//        db.collection("tags")
//                .add(tags)
//                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId()))
//                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
//
//


        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height);
        RectF rect = new RectF((float) size /10, 0, size, (float) (size * 9) /10); // Use RectF for the pie chart bounds

        float startAngle = 0;
        for (int i = 0; i < slicePercentages.length; i++) {
            float sweepAngle = (slicePercentages[i] / 100) * 360; // Convert percentage to angle
            paint.setColor(colors[i]);
            canvas.drawArc(rect, startAngle, sweepAngle, true, paint);
            startAngle += sweepAngle;
        }
    }


    public void convertDataIntoPercentages(){

    }
}
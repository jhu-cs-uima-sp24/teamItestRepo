package com.example.a5_sample;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import android.util.AttributeSet;

public class CustomPieChart extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float[] slicePercentages = {25, 25, 25, 25}; // Example percentages
    private int[] colors = {0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00}; // Red, Green, Blue, Yellow

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
        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height);
        RectF rect = new RectF(0, 0, size, size); // Use RectF for the pie chart bounds

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
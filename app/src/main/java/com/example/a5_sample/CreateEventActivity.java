package com.example.a5_sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.a5_sample.databinding.ActivityMainBinding;
import com.example.a5_sample.databinding.ActivityNewEventBinding;

public class CreateEventActivity extends AppCompatActivity {

    private ActivityNewEventBinding binding;
    Context cntx;

    private NumberPicker hourPicker, minutePicker, secondPicker;
    private Button tagButton, createButton, startButton, backButton;
    private Switch timerSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cntx = getApplicationContext();
        binding = ActivityNewEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View root = binding.getRoot();
        hourPicker = root.findViewById(R.id.numPickerHr);
        minutePicker = root.findViewById(R.id.numPickerMin);
        secondPicker = root.findViewById(R.id.numPickerSec);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(11);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);
        hourPicker.setWrapSelectorWheel(true);
        minutePicker.setWrapSelectorWheel(true);
        secondPicker.setWrapSelectorWheel(true);

        timerSwitch = (Switch)root.findViewById(R.id.timerSwitch);
        timerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                   hourPicker.setEnabled(false);
                   minutePicker.setEnabled(false);
                   secondPicker.setEnabled(false);

                } else {
                    hourPicker.setEnabled(true);
                    minutePicker.setEnabled(true);
                    secondPicker.setEnabled(true);
                }
            }
        });

        tagButton = (Button)root.findViewById(R.id.tagButton);
        backButton = (Button)root.findViewById(R.id.backButton);
        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow(v);
            }
        });


        startButton = root.findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start StopwatchActivity
                Intent intent = new Intent(CreateEventActivity.this, StopwatchActivity.class);
                // Start the activity
                startActivity(intent);
            }
        });
    }

    private void initPopWindow(View v) {
        View view = LayoutInflater.from(cntx).inflate(R.layout.new_event_popup, null, false);
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE)
                {
                    popWindow.dismiss();
                    return true;
                }

                return false;
            }
        });
        popWindow.showAsDropDown(v, 0, 0);
        Button gamingButton = view.findViewById(R.id.gamingButton);
        Button breakButton = view.findViewById(R.id.breakButton);
        Button studyButton = view.findViewById(R.id.studyButton);
        Button workOutButton = view.findViewById(R.id.workOutButton);
        gamingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_black_24dp, 0, 0, 0);
                tagButton.setBackgroundColor(getResources().getColor(R.color.task_blue));
                tagButton.setText("Gaming");
            }
        });
        breakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_black_24dp, 0, 0, 0);
                tagButton.setBackgroundColor(getResources().getColor(R.color.task_pink));
                tagButton.setText("Break");
            }
        });
        studyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_black_24dp, 0, 0, 0);
                tagButton.setBackgroundColor(getResources().getColor(R.color.task_green));
                tagButton.setText("Study");
            }
        });
        workOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_black_24dp, 0, 0, 0);
                tagButton.setBackgroundColor(getResources().getColor(R.color.task_yellow));
                tagButton.setText("Workout");
            }
        });
    }
}
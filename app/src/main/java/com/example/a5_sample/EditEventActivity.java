package com.example.a5_sample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a5_sample.databinding.ActivityEditEventBinding;

public class EditEventActivity extends AppCompatActivity {

    private ActivityEditEventBinding binding;
    private SharedPreferences myPrefs;
    private EditText titleEditText, descriptionEditText;
    Context cntx;
    private int pos;

    private NumberPicker hourPicker, minutePicker, secondPicker;
    private Button tagButton, createButton, saveButton;
    private ImageButton backButton;
    private Switch timerSwitch;
    private boolean isStopwatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cntx = getApplicationContext();
        myPrefs = cntx.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        int seconds = myPrefs.getInt("seconds",0);

        String titleString = myPrefs.getString("title","");
        String descriptionString = myPrefs.getString("description","");
        String tag = myPrefs.getString("tag","");
        isStopwatch = myPrefs.getBoolean("isStopwatch",false);
        pos = myPrefs.getInt("position",0);
        Task task = MainActivity.tasks.get(MainActivity.taskAdapter.findTask(titleString));
        int hours = seconds / 3600;
        int minutes = (seconds%3600) / 60;
        seconds = seconds - hours*3600 - minutes*60;
        SharedPreferences.Editor peditor = myPrefs.edit();
        binding = ActivityEditEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View root = binding.getRoot();
        hourPicker = root.findViewById(R.id.numPickerHrEdit);
        minutePicker = root.findViewById(R.id.numPickerMinEdit);
        secondPicker = root.findViewById(R.id.numPickerSecEdit);
        titleEditText = root.findViewById(R.id.editTextTitle);
        titleEditText.setText(titleString);

        descriptionEditText = root.findViewById(R.id.editTextDescription);
        descriptionEditText.setText(descriptionString);
        timerSwitch = root.findViewById(R.id.timerSwitch);
        timerSwitch.setChecked(isStopwatch);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(11);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);
        hourPicker.setWrapSelectorWheel(true);
        minutePicker.setWrapSelectorWheel(true);
        secondPicker.setWrapSelectorWheel(true);
        if (!isStopwatch) {
            hourPicker.setValue(hours);
            minutePicker.setValue(minutes);
            secondPicker.setValue(seconds);
        } else {
            hourPicker.setEnabled(false);
            minutePicker.setEnabled(false);
            secondPicker.setEnabled(false);
            hourPicker.setValue(0);
            minutePicker.setValue(0);
            secondPicker.setValue(0);
        }


        timerSwitch = (Switch)root.findViewById(R.id.timerSwitch);
        timerSwitch.setEnabled(false);
//        timerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                   hourPicker.setEnabled(false);
//                   minutePicker.setEnabled(false);
//                   secondPicker.setEnabled(false);
//                   isStopwatch = true;
//
//                } else {
//                    hourPicker.setEnabled(true);
//                    minutePicker.setEnabled(true);
//                    secondPicker.setEnabled(true);
//                    isStopwatch = false;
//                }
//            }
//        });

        tagButton = (Button)root.findViewById(R.id.tagButton);
        backButton = (ImageButton) root.findViewById(R.id.backButton);
        createButton = (Button)root.findViewById(R.id.createButton);
        saveButton = (Button)root.findViewById(R.id.saveButton);
        if (tag.equals("Gaming")) {
            tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.game, 0, 0, 0);
            tagButton.setBackgroundColor(getResources().getColor(R.color.task_blue));
            tagButton.setText("Gaming");
        } else if (tag.equals("Break")) {
            tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rest, 0, 0, 0);
            tagButton.setBackgroundColor(getResources().getColor(R.color.task_pink));
            tagButton.setText("Break");
        } else if (tag.equals("Study")) {
            tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.study, 0, 0, 0);
            tagButton.setBackgroundColor(getResources().getColor(R.color.task_green));
            tagButton.setText("Study");
        } else if (tag.equals("Workout")) {
            tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.workout, 0, 0, 0);
            tagButton.setBackgroundColor(getResources().getColor(R.color.task_yellow));
            tagButton.setText("Workout");
        }
        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow(v);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((MainActivity.taskAdapter.findTask(titleEditText.getText().toString())!=-1 && MainActivity.taskAdapter.findTask(titleEditText.getText().toString())!= pos) || MainActivity.completedTaskAdapter.findTask(titleEditText.getText().toString())!=-1) {
                    Toast.makeText(getApplicationContext(), "Task Name Must Be Unique!", Toast.LENGTH_SHORT).show();
                } else if (checkInput(isStopwatch)) {
                    int hour, minute, second;
                    hour = hourPicker.getValue();
                    minute = minutePicker.getValue();
                    second = secondPicker.getValue();
                    int time = hour * 3600 + minute * 60 + second;
                    String time_string = Integer.toString(time);
                    task.setTimeLeft(Integer.toString(time));
                    task.setTimeSpent(Integer.toString(0));
                    task.setTaskName(titleEditText.getText().toString());
                    task.setDescription(descriptionEditText.getText().toString());
                    task.setTag(tagButton.getText().toString());
                    MainActivity.taskAdapter.notifyDataSetChanged();
                    finish();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launch = new Intent(EditEventActivity.this, MainActivity.class);
                startActivity(launch);
            }
        });

    }

    private boolean checkInput(boolean isStopwatch) {
        int hour, minute, second;
        hour = hourPicker.getValue();
        minute = minutePicker.getValue();
        second = secondPicker.getValue();
        if (TextUtils.isEmpty(titleEditText.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Must have a title!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isStopwatch && hour == 0 && minute == 0 && second == 0) {
            Toast.makeText(getApplicationContext(), "Must select time duration!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (tagButton.getText().toString().equals("Add tag")) {
            Toast.makeText(getApplicationContext(), "Must select tag!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
                popWindow.dismiss();
            }
        });
        breakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_black_24dp, 0, 0, 0);
                tagButton.setBackgroundColor(getResources().getColor(R.color.task_pink));
                tagButton.setText("Break");
                popWindow.dismiss();
            }
        });
        studyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_black_24dp, 0, 0, 0);
                tagButton.setBackgroundColor(getResources().getColor(R.color.task_green));
                tagButton.setText("Study");
                popWindow.dismiss();
            }
        });
        workOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_black_24dp, 0, 0, 0);
                tagButton.setBackgroundColor(getResources().getColor(R.color.task_yellow));
                tagButton.setText("Workout");
                popWindow.dismiss();
            }
        });
    }
}
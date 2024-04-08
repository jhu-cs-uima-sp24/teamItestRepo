package com.example.LifePal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.example.LifePal.databinding.ActivityNewEventBinding;

public class CreateEventActivity extends AppCompatActivity {

    private ActivityNewEventBinding binding;
    private SharedPreferences myPrefs;
    private EditText titleEditText, descriptionEditText;
    Context cntx;

    private NumberPicker hourPicker, minutePicker, secondPicker;
    private Button tagButton, createButton, startButton;
    private ImageButton backButton;
    private Switch timerSwitch;
    private boolean isStopwatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cntx = getApplicationContext();
        myPrefs = cntx.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        SharedPreferences.Editor peditor = myPrefs.edit();
        binding = ActivityNewEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        View root = binding.getRoot();
        hourPicker = root.findViewById(R.id.numPickerHrEdit);
        minutePicker = root.findViewById(R.id.numPickerMinEdit);
        secondPicker = root.findViewById(R.id.numPickerSecEdit);
        titleEditText = root.findViewById(R.id.editTextTitle);
        descriptionEditText = root.findViewById(R.id.editTextDescription);
        timerSwitch = root.findViewById(R.id.timerSwitch);
        isStopwatch = false;

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
                   isStopwatch = true;

                } else {
                    hourPicker.setEnabled(true);
                    minutePicker.setEnabled(true);
                    secondPicker.setEnabled(true);
                    isStopwatch = false;
                }
            }
        });

        tagButton = (Button)root.findViewById(R.id.tagButton);
        backButton = (ImageButton) root.findViewById(R.id.backButton);
        createButton = (Button)root.findViewById(R.id.createButton);
        startButton = (Button)root.findViewById(R.id.startButton);
        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow(v);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.taskAdapter.findTask(titleEditText.getText().toString())!=-1 || MainActivity.completedTaskAdapter.findTask(titleEditText.getText().toString())!=-1) {
                    Toast.makeText(getApplicationContext(), "Task Name Must Be Unique!", Toast.LENGTH_SHORT).show();
                } else if (checkInput(isStopwatch)) {
                    int hour, minute, second;
                    hour = hourPicker.getValue();
                    minute = minutePicker.getValue();
                    second = secondPicker.getValue();
                    peditor.putString("title",titleEditText.getText().toString());
                    peditor.putInt("seconds",hour * 3600 + minute * 60 + second);
                    peditor.putString("description",descriptionEditText.getText().toString());
                    peditor.apply();
                    int time = hour * 3600 + minute * 60 + second;
                    String time_string = Integer.toString(time);
                    Task t = new Task(titleEditText.getText().toString(),descriptionEditText.getText().toString(),time_string,tagButton.getText().toString(), isStopwatch);
                    t.setStarted(true);
                    MainActivity.tasks.add(t);
                    MainActivity.taskAdapter.notifyDataSetChanged();
                    if (isStopwatch) {
                        Intent launch = new Intent(CreateEventActivity.this, StopwatchActivity.class);
                        int indexOf = MainActivity.tasks.size();
                        launch.putExtra("index", indexOf);
                        startActivity(launch);
                    } else {
                        Intent launch = new Intent(CreateEventActivity.this, TimerActivity.class);
                        startActivity(launch);
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launch = new Intent(CreateEventActivity.this, MainActivity.class);
                startActivity(launch);
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute, second;
                hour = hourPicker.getValue();
                minute = minutePicker.getValue();
                second = secondPicker.getValue();
                if (MainActivity.taskAdapter.findTask(titleEditText.getText().toString())!=-1) {
                    Toast.makeText(getApplicationContext(), "Task Name Must Be Unique!", Toast.LENGTH_SHORT).show();
                } else if (checkInput(isStopwatch)) {
                    int time = hour * 3600 + minute * 60 + second;
                    String time_string = Integer.toString(time);
                    MainActivity.tasks.add(new Task(titleEditText.getText().toString(),descriptionEditText.getText().toString(), time_string,tagButton.getText().toString(),isStopwatch));
                    MainActivity.taskAdapter.notifyDataSetChanged();
                    finish();
                }
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
                tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.game_bold, 0, 0, 0);
                tagButton.setBackgroundColor(getResources().getColor(R.color.task_blue));
                tagButton.setText("Gaming");
                popWindow.dismiss();
            }
        });
        breakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rest_bold, 0, 0, 0);
                tagButton.setBackgroundColor(getResources().getColor(R.color.task_pink));
                tagButton.setText("Break");
                popWindow.dismiss();
            }
        });
        studyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.study_bold, 0, 0, 0);
                tagButton.setBackgroundColor(getResources().getColor(R.color.task_green));
                tagButton.setText("Study");
                popWindow.dismiss();
            }
        });
        workOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.workout_bold, 0, 0, 0);
                tagButton.setBackgroundColor(getResources().getColor(R.color.task_yellow));
                tagButton.setText("Workout");
                popWindow.dismiss();
            }
        });
    }
}
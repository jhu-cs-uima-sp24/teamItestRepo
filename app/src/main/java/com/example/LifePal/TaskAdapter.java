package com.example.LifePal;


import static android.provider.Settings.System.getString;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class TaskAdapter extends ArrayAdapter<Task> {
    int resource;

    private FirebaseFirestore db;

    MainActivity myact;
    private SharedPreferences myPrefs;


    boolean inRoom;
    public TaskAdapter(Context ctx, int res, List<Task> taskList)
    {
        super(ctx, res, taskList);
        resource = res;
        myact = (MainActivity) ctx;
        inRoom = false;
        db = FirebaseFirestore.getInstance();
    }

    private void button_color_mapping(Context context, Button button, String availText) {
        switch (availText) {
            case "Study":
                button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.study_bold, 0, 0, 0);
                button.setBackgroundColor(ContextCompat.getColor(context,R.color.task_green));
                break;
            case "Break":
                button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rest_bold, 0, 0, 0);
                button.setBackgroundColor(ContextCompat.getColor(context,R.color.task_pink));
                break;
            case "Gaming":
                button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.game_bold, 0, 0, 0);
                button.setBackgroundColor(ContextCompat.getColor(context,R.color.task_blue));
                break;
            default:
                button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.workout_bold, 0, 0, 0);
                button.setBackgroundColor(ContextCompat.getColor(context,R.color.task_yellow));
                break;
        }
    }

    private String handleTimeSpentTimeLeft(TextView time, Task task) {
        int timeSpent = Integer.parseInt(task.getTimeSpent());
        int timeLeft =  Integer.parseInt(task.getTimeLeft());


        int hoursSpent, minutesSpent, secondsSpent, hoursLeft, minutesLeft, secondsLeft;
        hoursSpent = timeSpent / 3600;
        minutesSpent = (timeSpent%3600) / 60;
        secondsSpent = timeSpent - hoursSpent*3600 - minutesSpent*60;

        hoursLeft = timeLeft / 3600;
        minutesLeft = (timeLeft%3600) / 60;
        secondsLeft = timeLeft - hoursLeft*3600 - minutesLeft*60;
        String secondsLeftString = Integer.toString(secondsLeft);
        String secondsSpentString = Integer.toString(secondsSpent);
        if(secondsLeft < 10){
            secondsLeftString = "0" + secondsLeftString;
        }
        if(secondsSpent < 10){
            secondsSpentString = "0" + secondsSpentString;
        }
        if (task.getIsStopWatch()) {
            return Integer.toString(hoursSpent) + ":" + Integer.toString(minutesSpent) + ":" + secondsSpentString;
        } else if (task.getFinished()) {
            return Integer.toString(hoursSpent) + ":" + Integer.toString(minutesSpent) + ":" + secondsSpentString;
        } else {
            return Integer.toString(hoursLeft) + ":" + Integer.toString(minutesLeft) + ":" + secondsLeftString;
        }

    }

    public int findTask(String title) {
        for (int i = 0; i < getCount(); i++) {
            Task task = getItem(i);
            if (task != null && task.getTaskName().equals(title)) {

                return i;
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LinearLayout itemView;
        Task task = getItem(position);

        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }
        Context context = myact.getApplicationContext();
        SharedPreferences myPrefs = context.getSharedPreferences(context.getResources().getString(R.string.storage), Context.MODE_PRIVATE);
        SharedPreferences.Editor peditor = myPrefs.edit();
        String username = myPrefs.getString("username","");
        ImageButton editbutton = (android.widget.ImageButton) itemView.findViewById(R.id.imageButton3);

        ImageView taskIcon = (ImageView) itemView.findViewById(R.id.imageView);
        if (task.getIsStopWatch()) {
            taskIcon.setBackgroundResource(R.drawable.outline_alarm_24);
        } else {
            taskIcon.setBackgroundResource(R.drawable.ic_launcher_timer_foreground);
        }
        // Setting onClick behavior to the button
        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(context, editbutton);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.edit_button, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked

                            if (menuItem.getTitle().toString().equals("Delete Event")) {
//                                Toast.makeText(context, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                if (MainActivity.taskAdapter.findTask(task.getTaskName()) != -1) {
                                    db.collection("users").document(username).collection("tasks").document(task.getTaskName()).delete()
                                            .addOnSuccessListener(aVoid -> {
                                                return;
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete task", Toast.LENGTH_SHORT).show());
                                } else if (MainActivity.completedTaskAdapter.findTask(task.getTaskName()) != -1) {
                                    db.collection("users").document(username).collection("tasks").document(task.getTaskName()).delete()
                                            .addOnSuccessListener(aVoid -> {
                                                MainActivity.completedTasks.remove(MainActivity.completedTaskAdapter.findTask(task.getTaskName()));
                                                MainActivity.completedTaskAdapter.notifyDataSetChanged();
                                            })
                                            .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete task", Toast.LENGTH_SHORT).show());
                                } else {
                                    Toast.makeText(context, "This shouldn't happen", Toast.LENGTH_SHORT).show();
                                }
                            } else if (menuItem.getTitle().toString().equals("Edit Event")) {
                                Intent launch = new Intent(myact, EditEventActivity.class);
                                peditor.putString("title",task.getTaskName());
                                peditor.putInt("seconds", Integer.parseInt(task.getTimeLeft()));
                                peditor.putString("description",task.getDescription());
                                peditor.putString("tag",task.getTag());
                                peditor.putBoolean("isStopwatch",task.getIsStopWatch());
                                peditor.putInt("position",position);
                                peditor.putInt("Year",task.getYear());
                                peditor.putInt("Month",task.getMonth());
                                peditor.putInt("Day",task.getDay());
                                peditor.putInt("Hour",task.getHour());
                                peditor.putInt("Minute",task.getMinute());
                                peditor.putInt("Second",task.getSecond());
                                peditor.apply();
                                myact.startActivity(launch);
                            }
                            return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });


        TextView title = (TextView) itemView.findViewById(R.id.task_name_textview);
        title.setText(task.getTaskName());
        TextView description = (TextView) itemView.findViewById(R.id.description_textview);
        description.setText(task.getDescription());
        // Button tagButton = (Button) itemView.findViewById(R.id.tagButton);
        //        tagButton.setBackgroundColor(Color.RED);
//        button_color_mapping(tagButton, task.getTag());
        if(task.getFinished()){
            TextView start_finish = itemView.findViewById(R.id.start_finish_text_view);
            start_finish.setText("Finished");
            Drawable[] drawables = start_finish.getCompoundDrawables();
            Drawable leftDrawable = drawables[0]; // Change index as needed (0 = left, 1 = top, 2 = right, 3 = bottom)

// Tint the drawable (change its color)
            Drawable tintedDrawable = DrawableCompat.wrap(leftDrawable).mutate(); // Wrap the drawable to make it mutable
            DrawableCompat.setTint(tintedDrawable, Color.GREEN); // Change color as needed
            ImageButton editButton = itemView.findViewById(R.id.imageButton3);
            editButton.setVisibility(View.GONE);
        } else if(task.getStarted()){
            TextView start_finish = itemView.findViewById(R.id.start_finish_text_view);
            start_finish.setText("Resume");
        }
        TextView time = (TextView) itemView.findViewById(R.id.time_spent_text_view);
        String timeString = handleTimeSpentTimeLeft(time, task);
        String blockTimeString = "";
        int seconds = 0;
        if (task.getIsStopWatch()){
            blockTimeString = "Time Spent: ";
            seconds = Integer.parseInt(task.getTimeSpent());
        } else {
            //TODO:Figure this out
            blockTimeString = "Time Spent: ";
            seconds = Integer.parseInt(task.getTimeLeft());
        }
        blockTimeString += timeString;
       // Log.d("timeString", timeString);
        time.setText(blockTimeString);

        Button tagButton = (Button) itemView.findViewById(R.id.tag_button);
        button_color_mapping(context,tagButton, task.getTag());
        tagButton.setText(task.getTag());

        CardView cardView = (CardView) itemView.findViewById(R.id.card_view2);
        int finalSeconds = seconds;
        if(!task.getFinished()){
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (task.getIsStopWatch()) {
                        int position = findTask(task.getTaskName());
                        if (position != -1) {
                            Task task = getItem(position);
                            if (task != null) {
                                db.collection("users").document(username).collection("tasks").document(task.getTaskName()).update(
                                        "started",true
                                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                task.setStarted(true);
                                                MainActivity.taskAdapter.notifyDataSetChanged();
                                                peditor.putString("title",title.getText().toString());
                                                peditor.putInt("seconds", finalSeconds);
                                                peditor.putString("description",description.getText().toString());
                                                peditor.apply();
                                                Intent intent;
                                                intent = new Intent(myact, StopwatchActivity.class);
                                                myact.startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Failed to start task", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                    } else {
                        int position = findTask(task.getTaskName());
                        if (position != -1) {
                            Task task = getItem(position);
                            if (task != null) {
                                db.collection("users").document(username).collection("tasks").document(task.getTaskName()).update(
                                                "started",true
                                        ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                task.setStarted(true);
                                                MainActivity.taskAdapter.notifyDataSetChanged();
                                                Intent intent;
                                                peditor.putString("title",title.getText().toString());
                                                peditor.putInt("seconds", finalSeconds);
                                                peditor.putString("description",description.getText().toString());
                                                peditor.apply();
                                                intent = new Intent(myact, TimerActivity.class);
                                                myact.startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Failed to start task", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        }
                    }
                }
            });
        }


        return itemView;
    }
}



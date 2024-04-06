package com.example.a5_sample;


import android.content.Context;
import android.content.SharedPreferences;

import android.graphics.Color;
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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class TaskAdapter extends ArrayAdapter<Task> {
    int resource;
    MainActivity myact;
    boolean inRoom;
    public TaskAdapter(Context ctx, int res, List<Task> taskList)
    {
        super(ctx, res, taskList);
        resource = res;
        myact = (MainActivity) ctx;
        inRoom = false;
    }

    private void button_color_mapping(Context context, Button button, String availText) {
        switch (availText) {
            case "Study":
                button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_black_24dp, 0, 0, 0);
                button.setBackgroundColor(ContextCompat.getColor(context,R.color.task_green));
                break;
            case "Break":
                button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_black_24dp, 0, 0, 0);
                button.setBackgroundColor(ContextCompat.getColor(context,R.color.task_pink));
                break;
            case "Gaming":
                button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_black_24dp, 0, 0, 0);
                button.setBackgroundColor(ContextCompat.getColor(context,R.color.task_blue));
                break;
            default:
                button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_black_24dp, 0, 0, 0);
                button.setBackgroundColor(ContextCompat.getColor(context,R.color.task_yellow));
                break;
        }
    }

    private void handleTimeSpentTimeLeft(TextView time, Task task) {
        int timeSpent = task.getTimeSpent();
        int timeLeft = task.getTimeLeft();

        int hoursSpent, minutesSpent, secondsSpent, hoursLeft, minutesLeft, secondsLeft;
        hoursSpent = timeSpent / 3600;
        minutesSpent = (timeSpent%3600) / 60;
        secondsSpent = timeSpent - hoursSpent*3600 - minutesSpent*60;

        hoursLeft = timeLeft / 3600;
        minutesLeft = (timeLeft%3600) / 60;
        secondsLeft = timeLeft - hoursLeft*3600 - minutesLeft*60;

        if (task.getIsStopWatch()) {
            time.setText("Time Spent: " + hoursSpent + ":" + minutesSpent + ":" + secondsSpent);
        } else if (task.getFinished()) {
            time.setText("Time Spent: " + hoursLeft + ":" + minutesLeft + ":" + secondsLeft);
        } else if ((timeSpent > timeLeft)) {
            time.setText("Time Spent: " + hoursSpent + ":" + minutesSpent + ":" + secondsSpent);
        } else {
            time.setText("Time Left: " + hoursLeft + ":" + minutesLeft + ":" + secondsLeft);
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
                        Toast.makeText(context, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
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
        TextView time = (TextView) itemView.findViewById(R.id.time_spent_text_view);
        handleTimeSpentTimeLeft(time, task);
        Button tagButton = (Button) itemView.findViewById(R.id.tag_button);
        button_color_mapping(context,tagButton, task.getTag());
        tagButton.setText(task.getTag());

//        TextView name = (TextView) itemView.findViewById(R.id.room_location);
//        name.setText(rm.getname());
//        TextView size = (TextView) itemView.findViewById(R.id.room_size);
//        size.setText(rm.getoccupants() + " / " + rm.getcap());
//        Button checkin = (Button) itemView.findViewById(R.id.checkin);
//        String availText = rm.getchecked_in() ? out : in;
//        checkin.setText(availText);
//        inRoom = myPrefs.getBoolean("inRoom", false);
//        int inRoomPosition = myPrefs.getInt("position", 0);
//        if(inRoom) {
//            if (position == inRoomPosition) {
//                checkin.setText(out);
//            }
//        }
//        Log.d("inroom: ", Boolean.toString(inRoom));
//        Log.d("position: ", Integer.toString(position));
//        checkin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (checkin.getText().equals(out)) { // leaving room
//                    if (!inRoom) {
//                        Toast.makeText(myact.getApplicationContext(), "Not in a room!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        inRoom = false;
//                        checkin.setText(in);
//                        peditor.putBoolean("inRoom",false);
//                        peditor.apply();
//                        rm.setOccupants(rm.getoccupants() - 1);
//                        size.setText(rm.getoccupants() + " / " + rm.getcap());
//                    }
//                } else {
//                    if (inRoom) {
//                        Toast.makeText(myact.getApplicationContext(), "Already in another room!", Toast.LENGTH_SHORT).show();
//                    } else if (rm.getoccupants()+1>rm.getcap()) {
//                        Toast.makeText(myact.getApplicationContext(), "Room is full!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        inRoom = true;
//                        checkin.setText(out);
//                        peditor.putBoolean("inRoom",true);
//                        peditor.putInt("position",position);
//                        peditor.apply();
//                        rm.setOccupants(rm.getoccupants() + 1);
//                        size.setText(rm.getoccupants() + " / " + rm.getcap());
//                    }
//                }
//            }
//        });

        return itemView;
    }
}



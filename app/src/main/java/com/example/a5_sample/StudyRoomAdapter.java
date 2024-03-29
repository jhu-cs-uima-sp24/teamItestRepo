package com.example.a5_sample;


import android.content.Context;
import android.content.SharedPreferences;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.List;
import android.graphics.Color;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class StudyRoomAdapter extends ArrayAdapter<StudyRoom> {
    int resource;
    MainActivity myact;
    boolean inRoom;
    String out = getContext().getString(R.string.leave);
    String in = getContext().getString(R.string.enter);
    public StudyRoomAdapter(Context ctx, int res, List<StudyRoom> studyRoomList)
    {
        super(ctx, res, studyRoomList);
        resource = res;
        myact = (MainActivity) ctx;
        inRoom = false;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LinearLayout itemView;
        StudyRoom rm = getItem(position);

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

        TextView name = (TextView) itemView.findViewById(R.id.room_location);
        name.setText(rm.getname());
        TextView size = (TextView) itemView.findViewById(R.id.room_size);
        size.setText(rm.getoccupants() + " / " + rm.getcap());
        Button checkin = (Button) itemView.findViewById(R.id.checkin);
        String availText = rm.getchecked_in() ? out : in;
        checkin.setText(availText);
        inRoom = myPrefs.getBoolean("inRoom", false);
        int inRoomPosition = myPrefs.getInt("position", 0);
        if(inRoom) {
            if (position == inRoomPosition) {
                checkin.setText(out);
            }
        }
//        Log.d("inroom: ", Boolean.toString(inRoom));
//        Log.d("position: ", Integer.toString(position));
        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkin.getText().equals(out)) { // leaving room
                    if (!inRoom) {
                        Toast.makeText(myact.getApplicationContext(), "Not in a room!", Toast.LENGTH_SHORT).show();
                    } else {
                        inRoom = false;
                        checkin.setText(in);
                        peditor.putBoolean("inRoom",false);
                        peditor.apply();
                        rm.setOccupants(rm.getoccupants() - 1);
                        size.setText(rm.getoccupants() + " / " + rm.getcap());
                    }
                } else {
                    if (inRoom) {
                        Toast.makeText(myact.getApplicationContext(), "Already in another room!", Toast.LENGTH_SHORT).show();
                    } else if (rm.getoccupants()+1>rm.getcap()) {
                        Toast.makeText(myact.getApplicationContext(), "Room is full!", Toast.LENGTH_SHORT).show();
                    } else {
                        inRoom = true;
                        checkin.setText(out);
                        peditor.putBoolean("inRoom",true);
                        peditor.putInt("position",position);
                        peditor.apply();
                        rm.setOccupants(rm.getoccupants() + 1);
                        size.setText(rm.getoccupants() + " / " + rm.getcap());
                    }
                }
            }
        });

        return itemView;
    }
}



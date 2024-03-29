package com.example.a5_sample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewRoom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_room);

        Button sbtn = (Button) findViewById(R.id.save_btn);
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText location = (EditText) findViewById(R.id.new_room);
                String name = location.getText().toString();
                EditText size = (EditText) findViewById(R.id.new_cap);
                String capString = size.getText().toString();
                if (name.isEmpty() || capString.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "A field is empty!", Toast.LENGTH_SHORT).show();
                } else {
                    int cap = Integer.parseInt(capString);
                    MainActivity.rooms.add(new StudyRoom(name,cap));
                    MainActivity.roomsAdapter.notifyDataSetChanged();
                    finish();
                }
            }
        });

        Button qbtn = (Button) findViewById(R.id.quit_btn);
        qbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
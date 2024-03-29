package com.example.a5_sample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OpeningActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        Context context = getApplicationContext();
        EditText nameInput = (EditText) findViewById(R.id.editTextLoginName);
        SharedPreferences myPrefs = context.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        if (myPrefs.contains("LOGIN_name")) {
            nameInput.setText(myPrefs.getString("LOGIN_name",null));
        }
//        Button lbtn = (Button) findViewById(R.id.loginBtn);
//        lbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = String.valueOf(nameInput.getText());
//                if (name.isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "Name can't be empty!", Toast.LENGTH_SHORT).show();
//                } else {
//                    SharedPreferences.Editor editor = myPrefs.edit();
//                    editor.putString("LOGIN_name", name);
//                    editor.apply();
//                }
//            }
//        });

    }

    public void loginOnClick(View view) {
        EditText nameInput = (EditText) findViewById(R.id.editTextLoginName);
        Context context = getApplicationContext();
        SharedPreferences myPrefs = context.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        String name = String.valueOf(nameInput.getText());
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Name can't be empty!", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences.Editor editor = myPrefs.edit();
            editor.putString("LOGIN_name", name);
            //Log.d("login name", Objects.requireNonNull(myPrefs.getString("LOGIN_name", null)));
            editor.apply();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

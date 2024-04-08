package com.example.LifePal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.LifePal.databinding.ActivityOpeningBinding;

import com.example.LifePal.databinding.ActivityMainBinding;

public class OpeningActivity extends AppCompatActivity {

    private ActivityOpeningBinding binding;
    SharedPreferences.Editor peditor;
    SharedPreferences myPrefs;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_opening);
        Context context = getApplicationContext();
        myPrefs = context.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);

        binding = ActivityOpeningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            // If not added, replace the content with GetStarted fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.get_started, new GetStarted())
                    .commit();
        }

    }

}

package com.example.a5_sample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.a5_sample.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public static ArrayList<Task> tasks;

    public static ArrayList<Task> completedTasks;
    public static TaskAdapter taskAdapter;
    public static TaskAdapter completedTaskAdapter;
    public Task current;
    public Random randy = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();
        SharedPreferences myPrefs = context.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        SharedPreferences.Editor peditor = myPrefs.edit();
        peditor.putBoolean("inRoom",false);
        peditor.apply();

        tasks = new ArrayList<Task>();
        completedTasks = new ArrayList<Task>();
        tasks.add(new Task("UIMA HW", "Finish the UIMA homework", 10, "Study"));
        tasks.add(new Task("Other HW", "Finish the Other homework", 10, "Study"));
        tasks.add(new Task("UIMA HW", "Finish the UIMA homework", 10, "Study"));
        tasks.add(new Task("Other HW", "Finish the Other homework", 10, "Study"));
        tasks.add(new Task("UIMA HW", "Finish the UIMA homework", 10, "Study"));
        tasks.add(new Task("Other HW", "Finish the Other homework", 10, "Study"));
        tasks.add(new Task("UIMA HW", "Finish the UIMA homework", 10, "Study"));
        tasks.add(new Task("Other HW", "Finish the Other homework", 10, "Study"));
        taskAdapter = new TaskAdapter(this, R.layout.roomlayout, tasks);
        completedTaskAdapter = new TaskAdapter(this, R.layout.roomlayout, completedTasks);
        completedTasks.add(new Task("More HW", "Finish the More homework", 10, "Study"));
        completedTasks.add(new Task("Even More HW", "Finish the Even homework", 10, "Study"));
        completedTasks.add(new Task("More HW", "Finish the More homework", 10, "Study"));
        completedTasks.add(new Task("Even More HW", "Finish the Even homework", 10, "Study"));
        completedTasks.add(new Task("More HW", "Finish the More homework", 10, "Study"));
        completedTasks.add(new Task("Even More HW", "Finish the Even homework", 10, "Study"));
        current = null;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_profile, R.id.navigation_home)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        String name = myPrefs.getString("loginName", "Owner");

//        Toast.makeText(context.getApplicationContext(), "Created!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            logoutOnClick();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void logoutOnClick() {
        Intent intent = new Intent(this, OpeningActivity.class);
        startActivity(intent);
    }

}
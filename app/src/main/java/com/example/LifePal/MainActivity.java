package com.example.LifePal;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.LifePal.databinding.ActivityMainBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public static ArrayList<Task> tasks;

    public static ArrayList<Task> completedTasks;
    public static TaskAdapter taskAdapter;
    public static TaskAdapter completedTaskAdapter;
    public Task current;
    public Random randy = new Random();
    private Toolbar toolbar;
    private TextView toolbarText;
    private ConstraintLayout points;
    private TextView pts_name;
    private TextView pts_pts;
    private ImageView pet_pts;

    private int pet_id = 2131230854;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        Context context = getApplicationContext();
        SharedPreferences myPrefs = context.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        SharedPreferences.Editor peditor = myPrefs.edit();
        peditor.putBoolean("inRoom",false);
        peditor.apply();

        tasks = new ArrayList<Task>();
        completedTasks = new ArrayList<Task>();
//        tasks.add(new Task("UIMA HW", "Finish the UIMA homework", 10, "Study"));
//        tasks.add(new Task("Other HW", "Finish the Other homework", 10, "Study"));
//        tasks.add(new Task("UIMA HW", "Finish the UIMA homework", 10, "Study"));
//        tasks.add(new Task("Other HW", "Finish the Other homework", 10, "Study"));
//        tasks.add(new Task("UIMA HW", "Finish the UIMA homework", 10, "Study"));
//        tasks.add(new Task("Other HW", "Finish the Other homework", 10, "Study"));
//        tasks.add(new Task("UIMA HW", "Finish the UIMA homework", 10, "Study"));
//        tasks.add(new Task("Other HW", "Finish the Other homework", 10, "Study"));
        taskAdapter = new TaskAdapter(this, R.layout.roomlayout, tasks);
        completedTaskAdapter = new TaskAdapter(this, R.layout.roomlayout, completedTasks);
//        completedTasks.add(new Task("More HW", "Finish the More homework", 10, "Study"));
//        completedTasks.add(new Task("Even More HW", "Finish the Even homework", 10, "Study"));
//        completedTasks.add(new Task("More HW", "Finish the More homework", 10, "Study"));
//        completedTasks.add(new Task("Even More HW", "Finish the Even homework", 10, "Study"));
//        completedTasks.add(new Task("More HW", "Finish the More homework", 10, "Study"));
//        completedTasks.add(new Task("Even More HW", "Finish the Even homework", 10, "Study"));
        current = null;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        toolbar = binding.toolbar;
        toolbarText = binding.toolbarTitle;


//        setSupportActionBar(toolbar);
//        getSupportActionBar().hide();
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_profile, R.id.navigation_home)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        String name = myPrefs.getString("user_name", "Owner");

//        Toast.makeText(context.getApplicationContext(), "Created!", Toast.LENGTH_SHORT).show();




        points = binding.ptBar;
        pts_name = binding.petNamePts;
        pts_pts = binding.petPtsPts;
        pet_pts = binding.petPts;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String username = myPrefs.getString("username","");

        DocumentReference userDocRef = db.collection("users").document(username);

        userDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.d(TAG, "Current data: " + documentSnapshot.getData());

                    Map<String, Object> entry = documentSnapshot.getData();
                    if (entry != null) {
                        SharedPreferences.Editor peditor = myPrefs.edit();
                        peditor.putString("user_name", (String) entry.get("user_name"));
                        peditor.putString("pet_name", (String) entry.get("pet_name"));
                        peditor.putString("pet_type", (String) entry.get("pet_type"));
                        peditor.putString("user_goal", (String) entry.get("user_goal"));
                        peditor.putInt("current_points", Math.toIntExact((Long) entry.get("current_points")));
                        peditor.putInt("pet_id", pet_id + 1);

                        if(Math.toIntExact((Long) entry.get("current_points")) >= 1000 &&  Math.toIntExact((Long) entry.get("current_points")) < 2000) {
                            db.collection("users").document(username).update("pet_id", pet_id + 1);
                            pet_pts.setImageResource(pet_id + 1);
                        } else if (Math.toIntExact((Long) entry.get("current_points")) >= 2000 && Math.toIntExact((Long) entry.get("current_points")) < 3000) {
                            db.collection("users").document(username).update("pet_id", pet_id + 2);
                            pet_pts.setImageResource(pet_id + 2);
                        } else {
                            db.collection("users").document(username).update("pet_id", pet_id );
                            pet_pts.setImageResource(pet_id);
                        }
                      //  peditor.putInt("pet_id", Math.toIntExact((Long) entry.get("pet_id")));
                        peditor.putInt("next_level", Math.toIntExact((Long) entry.get("next_level")));
                        peditor.putInt("pet_level", Math.toIntExact((Long) entry.get("pet_level")));
                        peditor.apply();
                        String name_pts = myPrefs.getString("pet_name", null);
                        String pet_pts = Integer.toString(myPrefs.getInt("current_points", 0));
                        pts_name.setText("Current Pal: " + name_pts);
                        pts_pts.setText(pet_pts+" pts");

                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
        int pet_user = myPrefs.getInt("pet_id", -1);
        if (pet_user != -1) {
            pet_pts.setImageResource(pet_user);
        }
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Update Toolbar title based on the current fragment
            switch (destination.getId()) {
                case R.id.navigation_profile:
                    toolbarText.setText("Profile");
                    points.setVisibility(View.INVISIBLE);
                    toolbarText.setVisibility(View.VISIBLE);
                    break;
                case R.id.navigation_home:
                    toolbarText.setVisibility(View.INVISIBLE);
                    points.setVisibility(View.VISIBLE);
                    String name_pts = myPrefs.getString("pet_name", null);
                    String pet_pts = Integer.toString(myPrefs.getInt("current_points", 0));
                    pts_name.setText("Current Pal: " + name_pts);
                    pts_pts.setText(pet_pts+" pts");
                    break;
                default:
                    toolbarText.setText("Analytics");
                    points.setVisibility(View.INVISIBLE);
                    toolbarText.setVisibility(View.VISIBLE);
                    break;
            }
        });
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.logout) {
//            logoutOnClick();
//            return true;
//        } else {
//            return super.onOptionsItemSelected(item);
//        }
//    }
//
//    public void logoutOnClick() {
//        Intent intent = new Intent(this, OpeningActivity.class);
//        startActivity(intent);
//    }

}
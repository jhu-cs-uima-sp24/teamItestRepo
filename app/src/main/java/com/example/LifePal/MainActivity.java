package com.example.LifePal;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.LifePal.ui.stats.StatsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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
import java.util.Calendar;
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
    private ImageView pet_pts_pic;
    private boolean level1 = false;

    private boolean level2 = false;


    private int pet_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Bundle extras = getIntent().getExtras();
        Context context = getApplicationContext();
        SharedPreferences myPrefs = context.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        pet_id = myPrefs.getInt("pet_id", -1);
        SharedPreferences.Editor peditor = myPrefs.edit();
        peditor.putBoolean("inRoom",false);
        peditor.apply();

        tasks = new ArrayList<Task>();
        completedTasks = new ArrayList<Task>();
        taskAdapter = new TaskAdapter(this, R.layout.roomlayout, tasks);
        completedTaskAdapter = new TaskAdapter(this, R.layout.roomlayout, completedTasks);
        current = null;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        toolbar = binding.toolbar;
        toolbarText = binding.toolbarTitle;


        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_stats, R.id.navigation_profile)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.navigate(R.id.navigation_stats);
        Log.e("HomeId", String.valueOf(R.id.navigation_home));
        Log.e("StatsId", String.valueOf(R.id.navigation_stats));
        Log.e("ProfileId", String.valueOf(R.id.navigation_profile));

        String Str = "stats";
        if (getIntent().hasExtra(Str)) {
//            Log.w("MainActivity", "onCreate: " + "stats");
//            navView.post(() -> {
//                navController.navigate(R.id.navigation_stats);
//
//                new Handler().postDelayed(() -> {
//                    // Access destination here (might not be reliable)
//                }, 100); // 100 milliseconds delay
//            });
        }
        else{
            SharedPreferences sharedPreferences = getSharedPreferences("statsMode", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Calendar calendar = Calendar.getInstance();
            editor.putInt("yearVal", calendar.get(Calendar.YEAR));
            editor.putInt("monthVal", calendar.get(Calendar.MONTH));
            editor.putInt("dayVal", calendar.get(Calendar.DAY_OF_MONTH));
            editor.putString("mode", "day");
            Log.w("LoginActivity", "onCreate: " + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH));
            editor.apply();
        }

//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        String name = myPrefs.getString("user_name", "Owner");

//        Toast.makeText(context.getApplicationContext(), "Created!", Toast.LENGTH_SHORT).show();




        points = binding.ptBar;
        pts_name = binding.petNamePts;
        pts_pts = binding.petPtsPts;
        pet_pts_pic = binding.petPts;



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
                        peditor.putInt("pet_id", Math.toIntExact((Long) entry.get("pet_id")));

                        //  peditor.putInt("pet_id", Math.toIntExact((Long) entry.get("pet_id")));
                        peditor.putInt("next_level", Math.toIntExact((Long) entry.get("next_level")));
                        peditor.putInt("pet_level", Math.toIntExact((Long) entry.get("pet_level")));
                        peditor.apply();
                        String name_pts = myPrefs.getString("pet_name", null);
                        String pet_pts = Integer.toString(myPrefs.getInt("current_points", 0));
                        pts_name.setText("Current Pal: " + name_pts);
                        pts_pts.setText(pet_pts+" pts");
                        int pet_user = myPrefs.getInt("pet_id", -1);
                        if (pet_user != -1) {
                            pet_pts_pic.setImageResource(pet_user);
                        }

                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });



//        String Str = "stats";
//        if (getIntent().hasExtra(Str)) {
//            Log.w("MainActivity", "onCreate: " + "stats");
//            toolbarText.setText("Analytics");
//            points.setVisibility(View.INVISIBLE);
//            toolbarText.setVisibility(View.VISIBLE);
//            return;
//        }

//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navController.navigate(R.id.navigation_home);
        Log.e("HomeId", String.valueOf(R.id.navigation_home));
        Log.e("StatsId", String.valueOf(R.id.navigation_stats));
        Log.e("ProfileId", String.valueOf(R.id.navigation_profile));

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Update Toolbar title based on the current fragment

            Log.e("Destination", String.valueOf(destination.getId()));
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
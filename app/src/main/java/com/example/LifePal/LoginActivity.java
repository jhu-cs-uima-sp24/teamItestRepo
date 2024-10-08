package com.example.LifePal;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.LifePal.ui.stats.TagModel;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Context cntx;

    private SharedPreferences myPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w("LoginActivity", "onCreate: " + "called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


//        SharedPreferences sharedPreferences = getSharedPreferences("statsMode", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Calendar calendar = Calendar.getInstance();
//        editor.putInt("yearVal", calendar.get(Calendar.YEAR));
//        editor.putInt("monthVal", calendar.get(Calendar.MONTH));
//        editor.putInt("dayVal", calendar.get(Calendar.DAY_OF_MONTH));
//        Log.w("LoginActivity", "onCreate: " + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH));
//        editor.apply();




        cntx = getApplicationContext();

        myPrefs = getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        SharedPreferences.Editor peditor = myPrefs.edit();

        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        loginButton.setOnClickListener(v -> {
            String name = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!name.isEmpty() && !password.isEmpty()) {
                db.collection("allUsersLogins").document("credentials")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null && document.exists()) {
                                    String storedPassword = document.getString(name);
                                    if (password.equals(storedPassword)) {
//                                        SharedPreferences sharedPreferences = getSharedPreferences("statsMode", Context.MODE_PRIVATE);
//                                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                                        Calendar calendar = Calendar.getInstance();
//                                        editor.putInt("yearVal", calendar.get(Calendar.YEAR));
//                                        editor.putInt("monthVal", calendar.get(Calendar.MONTH));
//                                        editor.putInt("dayVal", calendar.get(Calendar.DAY_OF_MONTH));
//                                        Log.w("LoginActivity", "onCreate: " + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.MONTH) + " " + calendar.get(Calendar.DAY_OF_MONTH));
//                                        editor.apply();
                                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                        peditor.putString("username", name);
                                        peditor.apply();
                                        startActivity(new Intent(this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            }
        });

        signUpButton.setOnClickListener(v -> {
            startActivity(new Intent(this, OpeningActivity.class));
        });
    }
}
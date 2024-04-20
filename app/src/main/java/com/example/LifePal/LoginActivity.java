package com.example.LifePal;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.LifePal.ui.stats.TagModel;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashSet<String> allUsernames = new HashSet<>();
        Map<String, Object> allEntry = new HashMap<>();

        db.collection("allUsersLogins").document("credentials")
                .get().addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> entry = documentSnapshot.getData();
                    for (String s: entry.keySet()) {
                        allUsernames.add(s);
                        allEntry.put(s, entry.get(s));
                    }
                });





        loginButton.setOnClickListener(v -> {


            String name = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (!name.isEmpty() && !password.isEmpty() && allUsernames.contains(name) && allEntry.get(name).equals(password)) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                // Intent to start MainActivity
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else if (!name.isEmpty() && !password.isEmpty()) {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            }
        });

        signUpButton.setOnClickListener(v -> {
            startActivity(new Intent(this, OpeningActivity.class));
        });
    }
}
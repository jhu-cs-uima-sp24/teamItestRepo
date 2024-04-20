package com.example.LifePal;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        loginButton.setOnClickListener(v -> {
            String name = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (!name.isEmpty() && !password.isEmpty()) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                // Intent to start MainActivity
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            }
        });

        signUpButton.setOnClickListener(v -> {
            startActivity(new Intent(this, OpeningActivity.class));
        });
    }
}
package com.example.LifePal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;

import com.example.LifePal.databinding.FragmentGetStartedBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GetStarted extends Fragment {

    private FragmentGetStartedBinding binding;
    private SharedPreferences sharedPrefs;
    private Button loginButton, signUpButton;
    private EditText usernameInput, passwordInput;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGetStartedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPrefs = getActivity().getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        loginButton = binding.loginButton;
        signUpButton = binding.signUpButton;
        usernameInput = binding.usernameInput;
        passwordInput = binding.passwordInput;



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


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Replace the current fragment with the next fragment
                Fragment fragment = new EnterName();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.enter_name, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                root.setVisibility(View.GONE);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String name = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                if (!name.isEmpty() && !password.isEmpty() && allUsernames.contains(name) && allEntry.get(name).equals(password)) {
                    Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
                    // Intent to start MainActivity
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else if (!name.isEmpty() && !password.isEmpty()) {
                    Toast.makeText(getActivity(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
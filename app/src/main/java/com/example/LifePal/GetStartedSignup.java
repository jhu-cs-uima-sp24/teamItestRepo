package com.example.LifePal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.example.LifePal.databinding.FragmentGetStartedBinding;
import com.example.LifePal.databinding.FragmentGetStartedSignupBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GetStartedSignup extends Fragment {

    private FragmentGetStartedSignupBinding binding;
    private SharedPreferences sharedPrefs;
    private Button loginButton, signUpButton;
    private EditText usernameInput, passwordInput, passwordInputConfirmation;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGetStartedSignupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPrefs = getActivity().getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        signUpButton = binding.signUpButton;
        usernameInput = binding.usernameInput;
        passwordInput = binding.passwordInput;
        passwordInputConfirmation = binding.passwordInputConfirmation;



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

                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                String password_confirmation = passwordInputConfirmation.getText().toString();
                if (!username.isEmpty() && !password.isEmpty() && !password_confirmation.isEmpty()) {
                    if (!password.equals(password_confirmation)) {
                        Toast.makeText(getActivity(), "Password confirmation needs to be the same with password!", Toast.LENGTH_SHORT).show();
                    }else if (!allUsernames.contains(username)) {
                        Map<String, Object> newUser = new HashMap<>(allEntry);
                        newUser.put(username, password);
                        SharedPreferences myPrefs = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);

                        //TODO:Clearing sharedpreferences when signing up a new user?
//                                    myPrefs.edit().clear().apply();

                        SharedPreferences.Editor peditor = myPrefs.edit();
                        peditor.remove("password");
                        peditor.apply();

                        Fragment fragment = new EnterName();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.enter_name, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        root.setVisibility(View.GONE);


                    } else {
                        Toast.makeText(getActivity(), "Username already exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter both username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        loginButton.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View v) {
//                String name = usernameInput.getText().toString();
//                String password = passwordInput.getText().toString();
//                if (!name.isEmpty() && !password.isEmpty() && allUsernames.contains(name) && allEntry.get(name).equals(password)) {
//                    Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();
//                    // Intent to start MainActivity
//                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    startActivity(intent);
//                } else if (!name.isEmpty() && !password.isEmpty()) {
//                    Toast.makeText(getActivity(), "Invalid username or password", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), "Please enter email and password", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        return root;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
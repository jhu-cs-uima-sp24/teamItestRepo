package com.example.LifePal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.LifePal.databinding.FragmentEnterGoalBinding;
import com.example.LifePal.databinding.FragmentEnterNameBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class EnterGoal extends Fragment {
    private FragmentEnterGoalBinding binding;
    private EditText goal_edit;
    private String goal = "";
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor peditor;
    private Button nextButtonGoal;
    private Button backButtonGoal;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("tag", "name");
        binding = FragmentEnterGoalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPrefs = getActivity().getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        peditor = sharedPrefs.edit();

        goal_edit = binding.goalEdit;
        String goal_choosen = sharedPrefs.getString("user_goal", null);
        if (goal_choosen != null) {
            goal_edit.setText(goal_choosen);
            goal = goal_choosen;
        }
//        goal_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
//                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(goal_edit.getWindowToken(), 0);
//                    goal = goal_edit.getText().toString();
//                }
//                return false;
//            }
//        });
        goal_edit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && goal_edit.getText().length() == 0) {
                goal_edit.setHint("");
            } else if (!hasFocus && goal_edit.getText().length() == 0) {
                goal_edit.setHint("Goal");
            }
        });
        nextButtonGoal = binding.nextButton;
        nextButtonGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goal_edit.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter a valid goal!", Toast.LENGTH_SHORT).show();
                } else if (goal_edit.getText().toString().length() > 20) {
                    Toast.makeText(getActivity(), "Please enter a name that's 20 characters or less!", Toast.LENGTH_SHORT).show();
                } else {
                    goal = goal_edit.getText().toString();
                    peditor.putString("user_goal", goal);
                    peditor.apply();
                    String name = sharedPrefs.getString("user_name","");
                    String pet_name = sharedPrefs.getString("pet_name","");
                    String pet_type = sharedPrefs.getString("pet_type", "");
                    String user_goal = sharedPrefs.getString("user_goal", "");
                    String password = sharedPrefs.getString("password", "");
                    int pet_id = sharedPrefs.getInt("pet_id",-1);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String username = sharedPrefs.getString("username","");
                    Map<String, Object> allEntry = new HashMap<>();
                    allEntry.put("name",name);
                    allEntry.put("pet_name",pet_name);
                    allEntry.put("pet_type",pet_type);
                    allEntry.put("pet_id",pet_id);
                    allEntry.put("user_goal",user_goal);
                    allEntry.put("current_points",0);
                    allEntry.put("next_level",1000);
                    allEntry.put("pet_level",0);
                    Map<String, Object> newUser = new HashMap<>(allEntry);
                    newUser.put(username, password);
                    db.collection("allUsersLogins").document("credentials")
                            .update(newUser)
                            .addOnSuccessListener(atVoid -> {
                                Toast.makeText(getActivity(), "Signup successful", Toast.LENGTH_SHORT).show();
                                SharedPreferences myPrefs = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);

                                //TODO:Clearing sharedpreferences when signing up a new user?
//                                    myPrefs.edit().clear().apply();

                                SharedPreferences.Editor peditor = myPrefs.edit();
                                peditor.putString("username",username);
                                peditor.remove("user_name");
                                peditor.remove("pet_name");
                                peditor.remove("pet_type");
                                peditor.remove("pet_id");
                                peditor.remove("user_goal");
                                peditor.apply();

                                db.collection("users").document(username)
                                        .set(allEntry)
                                        .addOnSuccessListener(aVoid -> {
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            startActivity(intent);
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "Signup failed", Toast.LENGTH_SHORT).show());

                                Fragment fragment = new EnterName();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.enter_name, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                root.setVisibility(View.GONE);
                            })
                            .addOnFailureListener(e -> Toast.makeText(getActivity(), "Signup failed", Toast.LENGTH_SHORT).show());


                }

            }
        });
        backButtonGoal = binding.backButton;
        backButtonGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goal = goal_edit.getText().toString();
                peditor.putString("user_goal", goal);
                peditor.apply();
                Fragment fragment = new ChoosePet();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.enter_goal, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                root.setVisibility(View.GONE);
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
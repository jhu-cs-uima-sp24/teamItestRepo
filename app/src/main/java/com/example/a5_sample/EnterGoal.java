package com.example.a5_sample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a5_sample.databinding.FragmentEnterGoalBinding;
import com.example.a5_sample.databinding.FragmentEnterNameBinding;


public class EnterGoal extends Fragment {
    private FragmentEnterGoalBinding binding;
    private EditText goal_edit;
    private String goal = "";
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor peditor;
    private Button nextButtonName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("tag", "name");
        binding = FragmentEnterGoalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPrefs = getActivity().getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        peditor = sharedPrefs.edit();
        goal_edit = binding.goalEdit;
        goal_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(goal_edit.getWindowToken(), 0);
                    goal = goal_edit.getText().toString();
                }
                return false;
            }
        });
        goal_edit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && goal_edit.getText().length() == 0) {
                goal_edit.setHint("");
            } else if (!hasFocus && goal_edit.getText().length() == 0) {
                goal_edit.setHint("Goal");
            }
        });
        nextButtonName = binding.nextButton;
        nextButtonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goal.equals("")) {
                    Toast.makeText(getActivity(), "Please enter a valid goal!", Toast.LENGTH_SHORT).show();
                } else {
                    peditor.putString("user_goal", goal);
                    peditor.apply();
                    // Replace the current fragment with the next fragment
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
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
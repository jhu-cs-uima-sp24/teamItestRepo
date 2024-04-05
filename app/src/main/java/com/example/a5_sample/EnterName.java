package com.example.a5_sample;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
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
import android.content.SharedPreferences;
import androidx.fragment.app.FragmentManager;

import com.example.a5_sample.databinding.FragmentEnterNameBinding;


public class EnterName extends Fragment {

    private FragmentEnterNameBinding binding;
    private EditText name_edit;
    private String name = "";
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor peditor;
    private Button nextButtonName;
    private Button backButtonName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("tag", "name");
        binding = FragmentEnterNameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPrefs = getActivity().getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        peditor = sharedPrefs.edit();
        String savedName = sharedPrefs.getString("user_name", null);
        name_edit = binding.nameEdit;
        if (savedName != null) {
            name_edit.setText(savedName);
            name = savedName;
        }
        name_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(name_edit.getWindowToken(), 0);
                    name = name_edit.getText().toString();
                    if (!name.matches("[a-zA-Z]+")) {
                        // If the entered text contains characters other than letters,
                        // show a toast message
                        Toast.makeText(getActivity(), "Please enter a valid name!", Toast.LENGTH_SHORT).show();
                        // Clear the EditText
                        name_edit.setText("");
                        name = "";
                    }
                }
                return false;
            }
        });
        name_edit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && name_edit.getText().length() == 0) {
                name_edit.setHint("");
            } else if (!hasFocus && name_edit.getText().length() == 0) {
                name_edit.setHint("Name");
            }
        });
        nextButtonName = binding.nextButton;
        nextButtonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "onClick: " + name);
                if (name.equals("")) {
                    Toast.makeText(getActivity(), "Please enter a valid name!", Toast.LENGTH_SHORT).show();
                } else {
                    peditor.putString("user_name", name);
                    peditor.apply();
                    // Replace the current fragment with the next fragment
                    Fragment fragment = new PetName();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.enter_name, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            }
        });
        backButtonName = binding.backButton;
        backButtonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new GetStarted();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.enter_name, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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
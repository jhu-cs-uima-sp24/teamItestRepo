package com.example.LifePal;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;


import com.example.LifePal.databinding.FragmentEnterNameBinding;


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
//        name_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
//                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(name_edit.getWindowToken(), 0);
//                    name = name_edit.getText().toString();
//                    if (name.matches("^(?:\\\\W|\\\\s)*$")) {
//                        // If the entered text contains characters other than letters,
//                        // show a toast message
//                        Toast.makeText(getActivity(), "Please enter a valid name!", Toast.LENGTH_SHORT).show();
//                        // Clear the EditText
//                        name_edit.setText("");
//                        name = "";
//                    }
//                }
//                return false;
//            }
//        });
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
                if (name_edit.getText().toString().equals("") || !name_edit.getText().toString().matches("^[a-zA-Z]*$")) {
                    Toast.makeText(getActivity(), "Please enter a valid name!", Toast.LENGTH_SHORT).show();
                }
                else {
                    peditor.putString("user_name", name_edit.getText().toString());
                    peditor.apply();
                    // Replace the current fragment with the next fragment
                    Fragment fragment = new PetName();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.enter_name, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    root.setVisibility(View.GONE);
                }

            }
        });
        backButtonName = binding.backButton;
        backButtonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = name_edit.getText().toString();
                peditor.putString("user_name", name);
                peditor.apply();
                Fragment fragment = new GetStarted();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.enter_name, fragment);
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
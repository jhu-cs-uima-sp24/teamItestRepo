package com.example.a5_sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.example.a5_sample.databinding.FragmentPetNameBinding;


public class PetName extends Fragment {
    private FragmentPetNameBinding binding;
    private EditText pet_edit;
    private String pet_name = "";
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor peditor;
    private Button nextButtonPet;
    private Button backButtonPet;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPetNameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPrefs = getActivity().getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        peditor = sharedPrefs.edit();
        pet_edit = binding.petEdit;
        String savedPetName = sharedPrefs.getString("pet_name", null);
        if (savedPetName != null) {
            pet_edit.setText(savedPetName);
            pet_name = savedPetName;
        }
        pet_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(pet_edit.getWindowToken(), 0);
                    pet_name = pet_edit.getText().toString();
                    if (!pet_name.matches("[a-zA-Z]+")) {
                        // If the entered text contains characters other than letters,
                        // show a toast message
                        Toast.makeText(getActivity(), "Please enter a valid name!", Toast.LENGTH_SHORT).show();
                        // Clear the EditText
                        pet_edit.setText("");
                        pet_name = "";
                    }
                }
                return false;
            }
        });
        pet_edit.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && pet_edit.getText().length() == 0) {
                pet_edit.setHint("");
            } else if (!hasFocus && pet_edit.getText().length() == 0) {
                pet_edit.setHint("Name");
            }
        });
        nextButtonPet = binding.nextButtonPet;
        nextButtonPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "onClick: " + pet_name);
                if (pet_name.equals("")) {
                    Toast.makeText(getActivity(), "Please enter a valid name!", Toast.LENGTH_SHORT).show();
                } else {
                    peditor.putString("pet_name", pet_name);
                    peditor.apply();
                    // Replace the current fragment with the next fragment
                    Fragment fragment = new ChoosePet();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.pet_name, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    root.setVisibility(View.GONE);
                }

            }
        });

        backButtonPet = binding.backButton;
        backButtonPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EnterName();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.pet_name, fragment);
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
package com.example.a5_sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;

import com.example.a5_sample.databinding.FragmentGetStartedBinding;

public class GetStarted extends Fragment {

    private FragmentGetStartedBinding binding;
    private SharedPreferences sharedPrefs;
    Button getStartedButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGetStartedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedPrefs = getActivity().getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        getStartedButton = binding.getStartedButton;
        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goal = sharedPrefs.getString("user_goal", null);
                if (goal == null) {
                    // Replace the current fragment with the next fragment
                    Fragment fragment = new EnterName();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.enter_name, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    root.setVisibility(View.GONE);
                } else {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    /*Fragment fragment = new EnterName(); //for debugging
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.get_started, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    root.setVisibility(View.GONE);*/
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
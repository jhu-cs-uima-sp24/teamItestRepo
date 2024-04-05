package com.example.a5_sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a5_sample.databinding.FragmentChoosePetBinding;


public class ChoosePet extends Fragment {


    private FragmentChoosePetBinding binding;
    private int pet = -1;
    private String pet_type= "";
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor peditor;
    private Button nextButtonName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChoosePetBinding.inflate(inflater,container, false);
        View rootView = binding.getRoot();

        sharedPrefs = getActivity().getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        peditor = sharedPrefs.edit();

        // Populate images
        int[] imageResources = {R.drawable.dog_level0, R.drawable.cat_level0, R.drawable.mouse_level0, R.drawable.goat_level0};
        for (int resId : imageResources) {
            addImageToContainer(resId);
        }

        nextButtonName = binding.nextButtonChoose;
        nextButtonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pet == -1) {
                    Toast.makeText(getActivity(), "Please select a pet pal!", Toast.LENGTH_SHORT).show();
                } else {
                    pet_type = getResources().getResourceEntryName(pet);
                    peditor.putString("pet_type", pet_type);
                    peditor.apply();
                    // Replace the current fragment with the next fragment
                    Fragment fragment = new EnterGoal();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.choose_pet, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            }
        });

        return rootView;
    }

    private void addImageToContainer(int resId) {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 400);
        layoutParams.setMargins(0, 0, 16, 0);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(resId);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(v -> {
            binding.selectedImage.setImageResource(resId);
            binding.selectedImage.setVisibility(View.VISIBLE);
            pet = resId;
        });
        binding.imageContainer.addView(imageView);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullify the binding reference to avoid memory leaks
        binding = null;
    }

}
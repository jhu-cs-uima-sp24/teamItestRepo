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
    private Button nextButtonChoose;

    private Button backButtonChoose;
    private ImageView selected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentChoosePetBinding.inflate(inflater,container, false);
        View rootView = binding.getRoot();

        sharedPrefs = getActivity().getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        peditor = sharedPrefs.edit();
        // Populate images
        int[] imageResources = {R.drawable.rabbit_level0, R.drawable.dog_level0, R.drawable.cat_level0, R.drawable.mouse_level0, R.drawable.goat_level0, R.drawable.dog2_level0};
        for (int resId : imageResources) {
            addImageToContainer(resId);
        }
        int pet_choosen = sharedPrefs.getInt("pet_id", -1);
        if (pet_choosen != -1) {
            binding.selectedImage.setImageResource(pet_choosen);
            binding.selectedImage.setVisibility(View.VISIBLE);
            binding.imageContainer.setVisibility(View.INVISIBLE);
            binding.horizontalScrollView.setVisibility(View.INVISIBLE);
            binding.deselectionPrompt.setVisibility(View.VISIBLE);
            binding.bottomPawPrints.setVisibility(View.VISIBLE);
            binding.bottomPawPrintsCropped.setVisibility(View.INVISIBLE);
            pet = pet_choosen;
        } else {
            binding.bottomPawPrints.setVisibility(View.INVISIBLE);
            binding.bottomPawPrintsCropped.setVisibility(View.VISIBLE);
        }
        nextButtonChoose = binding.nextButtonChoose;
        nextButtonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pet == -1) {
                    Toast.makeText(getActivity(), "Please select a pet pal!", Toast.LENGTH_SHORT).show();
                } else {
                    pet_type = getResources().getResourceEntryName(pet);
                    peditor.putString("pet_type", pet_type);
                    peditor.putInt("pet_id", pet);
                    peditor.apply();
                    // Replace the current fragment with the next fragment
                    Fragment fragment = new EnterGoal();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.choose_pet, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    rootView.setVisibility(View.GONE);
                }

            }
        });

        backButtonChoose = binding.backButton;
        backButtonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PetName();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.choose_pet, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                rootView.setVisibility(View.GONE);
                if (pet == -1) {
                    peditor.putInt("pet_id", -1);
                    peditor.apply();
                }
            }
        });
        selected = binding.selectedImage;
        selected.setOnClickListener(v -> {
            binding.selectedImage.setVisibility(View.INVISIBLE); // Make the selected image invisible
            binding.horizontalScrollView.setVisibility(View.VISIBLE); // Make the HorizontalScrollView visible
            binding.imageContainer.setVisibility(View.VISIBLE); // Make the imageContainer visible
            binding.deselectionPrompt.setVisibility(View.INVISIBLE);
            binding.bottomPawPrints.setVisibility(View.INVISIBLE);
            binding.bottomPawPrintsCropped.setVisibility(View.VISIBLE);
            pet = -1;
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
            binding.imageContainer.setVisibility(View.INVISIBLE);
            binding.horizontalScrollView.setVisibility(View.INVISIBLE);
            binding.deselectionPrompt.setVisibility(View.VISIBLE);
            binding.bottomPawPrints.setVisibility(View.VISIBLE);
            binding.bottomPawPrintsCropped.setVisibility(View.INVISIBLE);
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
package com.example.a5_sample;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.view.animation.LinearInterpolator;

import com.example.a5_sample.databinding.FragmentLoadingPetsBinding;

public class LoadingPets extends Fragment {
    private FragmentLoadingPetsBinding binding;
    private ImageView paw;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("tag", "loading");
        binding = FragmentLoadingPetsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        paw = binding.loadingPaw;
        /*RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
        anim.setDuration(700);
        paw.startAnimation(anim);*/

        Fragment fragment = new ChoosePet();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.choose_pet, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        //paw.setAnimation(null);
        return root;
    }
}
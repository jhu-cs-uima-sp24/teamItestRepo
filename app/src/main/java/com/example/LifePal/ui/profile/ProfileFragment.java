package com.example.LifePal.ui.profile;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.example.LifePal.MainActivity;

import com.example.LifePal.R;

public class ProfileFragment extends Fragment {


    private MainActivity myact;
    Context cntx;

    private SharedPreferences myPrefs;
    private Handler handler = new Handler();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_profile, container, false);
        cntx = getActivity().getApplicationContext();
        myact = (MainActivity) getActivity();


        myPrefs = cntx.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);

        String username = myPrefs.getString("user_name","");

        int pet_user = myPrefs.getInt("pet_id", -1);
        ImageView pet_pic = myview.findViewById(R.id.petProfile_picture);
        if (pet_user != -1){
            pet_pic.setImageResource(pet_user);
        }


        ImageButton pet_profile_dropdown_button = myview.findViewById(R.id.pet_profile_dropdown_button);
        ImageButton pet_tags_dropdown_button = myview.findViewById(R.id.pet_tags_dropdown_button);
        ImageButton pet_personal_information_dropdown_button = myview.findViewById(R.id.pet_personal_information_dropdown_button);
        View pet_profile_dropdown = LayoutInflater.from(cntx).inflate(R.layout.fragment_profile_pet_dropdown, null, false);
        View pet_tags_dropdown = LayoutInflater.from(cntx).inflate(R.layout.fragment_tags_profile_dropdown, null, false);
        View pet_personal_information_dropdown = LayoutInflater.from(cntx).inflate(R.layout.fragment_profile_personal_information_dropdown, null, false);
//        View pet_tags_dropdown = myview.findViewById(R.id.pet_tags_dropdown);
//        View pet_personal_information_dropdown = myview.findViewById(R.id.pet_personal_information_dropdown);

        TextView usernameText = myview.findViewById(R.id.profile_title_text_view);
        usernameText.setText(username);

        pet_profile_dropdown_button.setOnClickListener(new View.OnClickListener() {
            PopupWindow popWindow;
            @Override
            public void onClick(View v) {
                Log.d("rotation", String.valueOf(v.getRotation()));
                if(v.getRotation() == 180f) {

                    ObjectAnimator.ofFloat(v, "rotation", 180f, 0f).start();
                    popWindow.dismiss();
                    //update bottom constraint
                    ImageView image = (ImageView) pet_profile_dropdown.findViewById(R.id.profile_pet_dropdown_image);
                   // ImageLoader imagLoader = new ImageLoader(getApplicationContext());
                    image.setImageResource(R.drawable.goat_level0);

                }
                else{
                    ObjectAnimator.ofFloat(v, "rotation", 0f, 180f).start();
                    popWindow = initPopWindow(v,pet_profile_dropdown);
                    //move element below lower without constraint
                    //ConstraintLayout constraintLayout = myview.findViewById(R.id.profile_constraint_layout);


                }

            }
        });

        pet_tags_dropdown_button.setOnClickListener(new View.OnClickListener() {
            PopupWindow popWindow;
            @Override
            public void onClick(View v) {
                if(v.getRotation() == 180f) {
                    ObjectAnimator.ofFloat(v, "rotation", 180f, 0f).start();
                     popWindow.dismiss();
                }
                else{
                    ObjectAnimator.ofFloat(v, "rotation", 0f, 180f).start();
                    popWindow = initPopWindow(v, pet_tags_dropdown);
                }

            }
        });

        pet_personal_information_dropdown_button.setOnClickListener(new View.OnClickListener() {
            PopupWindow popWindow;
            @Override
            public void onClick(View v) {
                if(v.getRotation() == 180f) {
                    popWindow.dismiss();
                    ObjectAnimator.ofFloat(v, "rotation", 180f, 0f).start();
                }
                else{
                    ObjectAnimator.ofFloat(v, "rotation", 0f, 180f).start();
                    popWindow = initPopWindow(v, pet_personal_information_dropdown);
                }

            }
        });

        return myview;
    }


    private PopupWindow initPopWindow(View v, View dropdown) {
        final PopupWindow popWindow = new PopupWindow(dropdown,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE)
                {
                    popWindow.dismiss();
                    return true;
                }

                return false;
            }
        });
        popWindow.showAsDropDown(v, 0, 0);
        return popWindow;
    }

}
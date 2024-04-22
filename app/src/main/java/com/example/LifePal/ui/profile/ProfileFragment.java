package com.example.LifePal.ui.profile;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.LifePal.GetStarted;
import com.example.LifePal.MainActivity;

import com.example.LifePal.OpeningActivity;
import com.example.LifePal.PetName;
import com.example.LifePal.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class ProfileFragment extends Fragment {


    private MainActivity myact;

    private FirebaseFirestore db;
    Context cntx;

    private SharedPreferences myPrefs;
    private Handler handler = new Handler();
    private ProgressBar petProgress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_profile, container, false);
        cntx = getActivity().getApplicationContext();
        myact = (MainActivity) getActivity();

        db = FirebaseFirestore.getInstance();

        myPrefs = cntx.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);

        String username = myPrefs.getString("username","");
        String usernameReal = myPrefs.getString("user_name","");
        String petname = myPrefs.getString("pet_name","");
        int points = myPrefs.getInt("current_points", 0);
        int next_level_points = myPrefs.getInt("next_level", 0);


        int pet_user = myPrefs.getInt("pet_id", -1);
        ImageView pet_pic = myview.findViewById(R.id.petProfile_picture);
        ImageButton pet_profile_dropdown_button = myview.findViewById(R.id.pet_profile_dropdown_button);
        ImageButton pet_tags_dropdown_button = myview.findViewById(R.id.pet_tags_dropdown_button);
        ImageButton pet_personal_information_dropdown_button = myview.findViewById(R.id.pet_personal_information_dropdown_button);
        View pet_profile_dropdown = LayoutInflater.from(cntx).inflate(R.layout.fragment_profile_pet_dropdown, null, false);
        ImageView pet_pic_dropdown = pet_profile_dropdown.findViewById(R.id.profile_pet_dropdown_image);
        EditText pet_name_dropdown = pet_profile_dropdown.findViewById(R.id.pet_name_text_view);
        TextView pet_points_dropdown = pet_profile_dropdown.findViewById(R.id.current_happniess_pet_text_view);
        TextView pet_points_next_level = pet_profile_dropdown.findViewById(R.id.points_till_next_level_text_view);
        View pet_tags_dropdown = LayoutInflater.from(cntx).inflate(R.layout.fragment_tags_profile_dropdown, null, false);
        View pet_personal_information_dropdown = LayoutInflater.from(cntx).inflate(R.layout.fragment_profile_personal_information_dropdown, null, false);

        TextView name = myview.findViewById(R.id.profile_title_text_view);
        EditText pet_name = pet_profile_dropdown.findViewById(R.id.pet_name_text_view);
        petProgress = pet_profile_dropdown.findViewById(R.id.PetProgressBar);
        TextView pet_level = pet_profile_dropdown.findViewById(R.id.level_text_view);
        TextView current_points = pet_profile_dropdown.findViewById(R.id.current_happniess_pet_text_view);
        TextView next_level = pet_profile_dropdown.findViewById(R.id.points_till_next_level_text_view);
        EditText user_name = pet_personal_information_dropdown.findViewById(R.id.user_name_edit_text_view);
        EditText goal = pet_personal_information_dropdown.findViewById(R.id.current_goal_text_view);
        ImageView pet_pic_dropdown_picture = pet_profile_dropdown.findViewById(R.id.profile_pet_dropdown_image);
        ImageView main_pet_pic = myview.findViewById(R.id.petProfile_picture);

        Button logout_button = myview.findViewById(R.id.logout_button);

        //db.collection("users").document()

        if (pet_user != -1 && !petname.equals("")){
            pet_pic.setImageResource(pet_user);
            pet_pic_dropdown.setImageResource(pet_user);
            pet_name_dropdown.setText(petname);
            String curr_happ = "Current Happniess Points: " + String.valueOf(points) + " pts";
            pet_points_dropdown.setText(curr_happ);
            float percentage = ((float)points/(float)next_level_points) * 100;
            petProgress.setProgress((int) percentage);
            String next_happ = String.valueOf(next_level_points - points) + " pts until the next evolution";
            pet_points_next_level.setText(next_happ);
        }
        TextView usernameText = myview.findViewById(R.id.profile_title_text_view);
        usernameText.setText(username);
        user_name.setText(username);

        db.collection("users").document(username).get().addOnSuccessListener(documentSnapshot -> {
            pet_name.setText(documentSnapshot.getString("pet_name"));
            goal.setText(documentSnapshot.getString("user_goal"));
            String pet_level_string_builder = "Pet Level: " + documentSnapshot.get("pet_level").toString();
            pet_level.setText(pet_level_string_builder);
            String current_point_string_builder = "Current Happiness Points: " +
                    documentSnapshot.get("current_points").toString();
            current_points.setText(current_point_string_builder);
            String next_level_string_builder = "Points until next level: " +
                    documentSnapshot.get("next_level").toString();
            next_level.setText(next_level_string_builder);
           // Log.d("pet_id", documentSnapshot.getLong("pet_id").toString());
           // Log.d("pet_id", String.valueOf(myPrefs.getInt("pet_id", 0)));
            pet_pic_dropdown_picture.setImageResource(documentSnapshot.getLong("pet_id").intValue());
            main_pet_pic.setImageResource(documentSnapshot.getLong("pet_id").intValue());

                });

        DocumentReference userDocRef = db.collection("users").document(username);

        userDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, com.google.firebase.firestore.FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.d("pet_id", documentSnapshot.getLong("pet_id").toString());
                    Log.d("pet_id", String.valueOf(myPrefs.getInt("pet_id", 0)));
                    pet_pic_dropdown_picture.setImageResource(documentSnapshot.getLong("pet_id").intValue());
                    main_pet_pic.setImageResource(documentSnapshot.getLong("pet_id").intValue());
                }
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor peditor = myPrefs.edit();
                peditor.clear();
                peditor.apply();
//                Fragment fragment = new GetStarted();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.get_started, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                Intent intent = new Intent(getActivity(), OpeningActivity.class);
                startActivity(intent);

            }
        });

        pet_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                updateFirestore("pet_name", s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }
        });

        user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                updateFirestore("username", s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }
        });

        goal.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                updateFirestore("goal", s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }
        });


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


    private void updateFirestore(String field, String value){
        db.collection("users").document(myPrefs.getString("username","")).update(field, value);
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
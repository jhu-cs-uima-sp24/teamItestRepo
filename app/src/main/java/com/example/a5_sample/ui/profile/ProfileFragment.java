package com.example.a5_sample.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
// import androidx.lifecycle.ViewModelProvider;

import com.example.a5_sample.R;
import com.example.a5_sample.databinding.FragmentProfileBinding;

import java.util.Objects;
// import com.example.a5_sample.ui.profile.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Button btn;
    private EditText student;
    private EditText majors;
    private EditText year;
    private EditText age;
    private SharedPreferences myPrefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // eliminated for simplicity
  //      ProfileViewModel dashboardViewModel =
  //              new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btn = binding.save;
        student = binding.editTextStudent;
        majors = binding.editTextMajors;
        year = binding.editTextYear;
        age = binding.editTextAge;

        Context context = getActivity().getApplicationContext();
        myPrefs = context.getSharedPreferences(getString(R.string.storage), Context.MODE_PRIVATE);
        if (myPrefs.contains("MY_name") && !Objects.requireNonNull(myPrefs.getString("MY_name", null)).isEmpty()) {
            //Log.d("my name", Objects.requireNonNull(myPrefs.getString("MY_name", null)));
            student.setText(myPrefs.getString("MY_name",null));
        } else if (myPrefs.contains("LOGIN_name")) {
            //Log.d("login name", Objects.requireNonNull(myPrefs.getString("LOGIN_name", null)));
            student.setText(myPrefs.getString("LOGIN_name",null));
        } else {
            Toast.makeText(context.getApplicationContext(), "Error getting profile name!", Toast.LENGTH_SHORT).show();
        }

        if (myPrefs.contains("MY_majors") && !Objects.requireNonNull(myPrefs.getString("MY_majors", null)).isEmpty()) {
            majors.setText(myPrefs.getString("MY_majors",null));
        }

        if (myPrefs.contains("MY_year") && !Objects.requireNonNull(myPrefs.getString("MY_year", null)).isEmpty()) {
            year.setText(myPrefs.getString("MY_year",null));
        }

        if (myPrefs.contains("MY_age") && !Objects.requireNonNull(myPrefs.getString("MY_age", null)).isEmpty()) {
            age.setText(myPrefs.getString("MY_age",null));
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor peditor = myPrefs.edit();
                peditor.putString("MY_name", String.valueOf(student.getText()));
                peditor.putString("MY_majors", String.valueOf(majors.getText()));
                peditor.putString("MY_year", String.valueOf(year.getText()));
                peditor.putString("MY_age", String.valueOf(age.getText()));
                peditor.apply();
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
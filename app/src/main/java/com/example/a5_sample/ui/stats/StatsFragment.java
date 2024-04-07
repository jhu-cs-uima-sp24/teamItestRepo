package com.example.a5_sample.ui.stats;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.a5_sample.R;
import com.example.a5_sample.databinding.FragmentStatsBinding;

import java.util.Calendar;

public class StatsFragment extends Fragment {

    private FragmentStatsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        StatsViewModel dashboardViewModel =
//                new ViewModelProvider(this).get(StatsViewModel.class);
//
//        binding = FragmentStatsBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//
//
//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;

        binding = FragmentStatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (savedInstanceState == null) {
            StatsDataFragment firstFragment = new StatsDataFragment();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainerView, firstFragment, "StatsDataFragment")
                    .commit();

            StatsPieChartFragment secondFragment = new StatsPieChartFragment();
            getChildFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainerView2, secondFragment, "StatsPieChartFragment")
                    .commit();
        }

        final Calendar c = Calendar.getInstance();

        Button button = (Button) root.findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click
                // For example, show a DatePickerDialog
                DatePickerFragment currentFragment = new DatePickerFragment();
                currentFragment.show(getParentFragmentManager(), "datePicker");
            }
        });



//        Button button = getActivity().findViewById(R.id.dateButton);
//        button.setOnClickListener(v -> showTimePickerDialog());
//


        return root;
    }
//    private void showTimePickerDialog() {
//        // Use the current time as the default values for the picker
//        final Calendar c = Calendar.getInstance();
//
//        findViewById<Button>(R.id.button4).setOnClickListener {
//            val newFragment = DatePickerFragment();
//            newFragment.show(supportFragmentManager, "datePicker");
//        }
//
//        // Create and show a new instance of TimePickerDialog
////        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute,
////                DateFormat.is24HourFormat(getActivity()));
////        timePickerDialog.show();
//    }

//    @Override
//    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        // Do something with the time chosen by the user
//        Log.d("TimePicker", "Hour: " + hourOfDay + " Minute: " + minute);
//    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker.
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it.
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date the user picks.
        }
    }

}
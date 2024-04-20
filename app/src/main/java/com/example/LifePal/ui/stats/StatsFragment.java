package com.example.LifePal.ui.stats;

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

import com.example.LifePal.R;
import com.example.LifePal.databinding.FragmentStatsBinding;
import com.example.LifePal.ui.stats.StatsDataFragment;
import com.example.LifePal.ui.stats.StatsPieChartFragment;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class StatsFragment extends Fragment {

    private FragmentStatsBinding binding;
    private static View root;

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
        root = binding.getRoot();

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
        button.setText("Date: " + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR));

        DatePickerFragment currentFragment = new DatePickerFragment();
        currentFragment.setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click
                // For example, show a DatePickerDialog
                currentFragment.show(getParentFragmentManager(), "datePicker");

            }
        });



//        Button button = getActivity().findViewById(R.id.dateButton);
//        button.setOnClickListener(v -> showTimePickerDialog());

//

        Button button2 = (Button) root.findViewById(R.id.day_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String currentDate = Calendar.DATE + "," + Calendar.MONTH + "," + Calendar.YEAR;
                String time = Calendar.HOUR + ":" + Calendar.MINUTE + ":" + Calendar.SECOND;
                String tagid = currentDate + " " + time;
                db.collection("tags").document("study").update(tagid, 20);
                db.collection("tags").document("workout").update(tagid, 30);
                db.collection("tags").document("gaming").update(tagid, 40);
                db.collection("tags").document("break").update(tagid, 10);
            }
        });


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


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        int CurrentPickedYear;
        int CurrentPickedMonth;
        int CurrentPickedDay;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(requireContext(), this, CurrentPickedYear, CurrentPickedMonth, CurrentPickedDay);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date the user picks.
            Button thisButton = (Button) root.findViewById(R.id.button6);
            setDate(year, month, day);
            thisButton.setText("Date: " + (month + 1) + "/" + day + "/" + year);
        }

        public void setDate(int year, int month, int day) {
            CurrentPickedYear = year;
            CurrentPickedMonth = month;
            CurrentPickedDay = day;
        }
    }

}
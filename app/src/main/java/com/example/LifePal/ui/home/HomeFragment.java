package com.example.LifePal.ui.home;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.example.LifePal.CreateEventActivity;
import com.example.LifePal.MainActivity;
import com.example.LifePal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    private ListView myList;

    private ListView completedList;
    private MainActivity myact;

    public static final int MENU_ITEM_EDITVIEW = Menu.FIRST;
    public static final int MENU_ITEM_DELETE = Menu.FIRST + 1;

    Context cntx;
 //   Park current;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // NOTE: HomeViewModel elements eliminated from original template

        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_home, container, false);

        cntx = getActivity().getApplicationContext();
        myact = (MainActivity) getActivity();

        myList = (ListView) myview.findViewById(R.id.toDoList);
        myList.setAdapter(myact.taskAdapter);
        myact.taskAdapter.notifyDataSetChanged();
        completedList = (ListView) myview.findViewById(R.id.completedList);
        completedList.setAdapter(myact.completedTaskAdapter);
        myact.completedTaskAdapter.notifyDataSetChanged();
      //  myview.findViewById(R.id.header).findViewById(R.id.imageButton2).setVisibility(View.GONE);
        completedList.setVisibility(View.GONE);
        //TextView completedTasksTextview = myview.findViewById(R.id.header).findViewById(R.id.task_name_textview);
       // Log.d("completedTasksTextview", completedTasksTextview.getText().toString());
        //  completedTasksTextview.setText("Finished Sessions");

        FloatingActionButton fab = (FloatingActionButton) myview.findViewById(R.id.fab);
        Drawable drawable = ContextCompat.getDrawable(cntx, R.drawable.baseline_add_48);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, getResources().getColor(R.color.white));
        fab.setImageDrawable(drawable);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launch = new Intent(myact, CreateEventActivity.class);
                startActivity(launch);
            }
        });


        // Setting onClick behavior to the button

        ImageButton dropbutton = myview.findViewById(R.id.header2).findViewById(R.id.imageButton2);

        dropbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getRotation() == 180f) {
                    ObjectAnimator.ofFloat(view, "rotation", 180f, 0f).start();
                    if(MainActivity.completedTasks.isEmpty()){
                        return;
                    }
                    completedList.setVisibility(View.GONE);
                    ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) myList.getLayoutParams();
                    lp.height = (int) (lp.height * 2);
                    myList.setLayoutParams(lp);
                    ViewGroup.LayoutParams lp2 = (ViewGroup.LayoutParams) completedList.getLayoutParams();
                    lp2.height = (int) (lp2.height / 2);
                    completedList.setLayoutParams(lp2);
                   // myview.findViewById(R.id.header).findViewById(R.id.imageButton2).setVisibility(View.GONE);
                }
                else{
                    ObjectAnimator.ofFloat(view, "rotation", 0f, 180f).start();
                    if(MainActivity.completedTasks.isEmpty()){
                        return;
                    }
                    view.animate().alpha(1.0f);
                    completedList.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) myList.getLayoutParams();
                    lp.height = (int) (lp.height/2);
                    myList.setLayoutParams(lp);
                    ViewGroup.LayoutParams lp2 = (  ViewGroup.LayoutParams) completedList.getLayoutParams();
                    lp2.height = lp2.height * 2;
                    completedList.setLayoutParams(lp2);
                  //  myview.findViewById(R.id.header).findViewById(R.id.imageButton2).setVisibility(View.VISIBLE);
                    //show list of elements by moving header2 up
//                    ViewGroup.LayoutParams params = myview.findViewById(R.id.completedList).getLayoutParams();
//                    params.height = 6
//                            + (150 * 6);
//                    myview.setLayoutParams(params);
//                    myview.findViewById(R.id.completedList).requestLayout();

                }

            }
        });
//
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference docRef = db.collection("tags").document("user1");
//
//        Map<String, Integer> tags = new HashMap<>();
//        tags.put("break", 10);
//        tags.put("study", 10);
//        tags.put("gaming", 20);
//        tags.put("workout", 100);
//
//        docRef.set(tags);


        return myview;
    }





}
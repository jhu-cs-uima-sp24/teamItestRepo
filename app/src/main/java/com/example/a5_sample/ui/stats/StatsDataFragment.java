package com.example.a5_sample.ui.stats;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.*;

import com.example.a5_sample.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsDataFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private HashMap<String, Integer> tagMap = new HashMap<>();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView recyclerView;
    private TagAdapter adapter;
    private List<TagModel> tags;

    public StatsDataFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatsDataFragment.
     */
    public static StatsDataFragment newInstance(String param1, String param2) {
        StatsDataFragment fragment = new StatsDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_stats_data, container, false);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        recyclerView = view.findViewById(R.id.tagsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tags = getTags(); // Implement this method to get your data
        adapter = new TagAdapter(tags, screenHeight);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(0));
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        int spanCount = 2; // 2 columns
        int spacing = 50;
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        return view;

    }


    private List<TagModel> getTags() {
        List<TagModel> list = new ArrayList<>();

        list.add(new TagModel("Break", 100));
        list.add(new TagModel("Study", 100));
        list.add(new TagModel("Gaming", 100));
        list.add(new TagModel("Workout", 100));

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("tags").document("user1")
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        Map<String, Long> entry = (Map<String, Long>) document.get("tags");
//                        for (String s: entry.keySet()) {
//                            list.add(new TagModel(s, entry.get(s).intValue()));
//                        }
//                    }
//                });

        return list;
    }
}
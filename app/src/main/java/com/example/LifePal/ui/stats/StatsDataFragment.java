package com.example.LifePal.ui.stats;

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

import com.example.LifePal.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;

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

    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        List<TagModel> list = new ArrayList<>();


        db.collection("tags").document("break")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot,
                                        FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG1", "Listen failed.", e);
                            return;
                        }
                        int totalTime = 0;
                        Map<String, Object> entry = documentSnapshot.getData();
                        if (entry != null) {
                            for (String s : entry.keySet()) {
                                Object value = entry.get(s);
                                if (value instanceof Long) {
                                    totalTime += ((Long) value).intValue();
                                } else if (value instanceof Double) {
                                    totalTime += ((Double) value).intValue();
                                }
                            }
                        }

                        list.add(new TagModel("Break", totalTime));
                        Log.w("TAGbreak", " list size: " + list.size());
                    }
                });

        db.collection("tags").document("study")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot,
                                        FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG1", "Listen failed.", e);
                            return;
                        }
                        int totalTime = 0;
                        Map<String, Object> entry = documentSnapshot.getData();
                        if (entry != null) {
                            for (String s : entry.keySet()) {
                                Object value = entry.get(s);
                                if (value instanceof Long) {
                                    totalTime += ((Long) value).intValue();
                                } else if (value instanceof Double) {
                                    totalTime += ((Double) value).intValue();
                                }
                            }
                        }

                        list.add(new TagModel("Study", totalTime));
                        Log.w("TAGstudy", " list size: " + list.size());
                    }
                });


        db.collection("tags").document("gaming")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot,
                                        FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG1", "Listen failed.", e);
                            return;
                        }
                        int totalTime = 0;
                        Map<String, Object> entry = documentSnapshot.getData();
                        if (entry != null) {
                            for (String s : entry.keySet()) {
                                Object value = entry.get(s);
                                if (value instanceof Long) {
                                    totalTime += ((Long) value).intValue();
                                } else if (value instanceof Double) {
                                    totalTime += ((Double) value).intValue();
                                }
                            }
                        }

                        list.add(new TagModel("Gaming", totalTime));
                        Log.w("TAGbreak", " list size: " + list.size());
                    }
                });


        db.collection("tags").document("workout")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot,
                                        FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG1", "Listen failed.", e);
                            return;
                        }
                        int totalTime = 0;
                        Map<String, Object> entry = documentSnapshot.getData();
                        if (entry != null) {
                            for (String s : entry.keySet()) {
                                Object value = entry.get(s);
                                if (value instanceof Long) {
                                    totalTime += ((Long) value).intValue();
                                } else if (value instanceof Double) {
                                    totalTime += ((Double) value).intValue();
                                }
                            }
                        }

                        list.add(new TagModel("Workout", totalTime));
                        Log.w("TAGbreak", " list size: " + list.size());


                        tags = list;// Implement this method to get your data
                        Log.w("Tag size outside", " list size: " + tags.size());
                        adapter = new TagAdapter(tags, screenHeight);
                        recyclerView.setAdapter(adapter);
                        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(0));
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        int spanCount = 2; // 2 columns
                        int spacing = 90;
                        boolean includeEdge = true;
                        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                    }
                });






        return view;

    }

}
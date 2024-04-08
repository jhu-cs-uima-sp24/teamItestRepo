package com.example.LifePal.ui.stats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.LifePal.R;

import java.util.*;

public class TagAdapter extends RecyclerView.Adapter<TagViewHolder> {
    private List<TagModel> contactList;
    private int screenHeight;

    public TagAdapter(List<TagModel> contactList, int screenHeight) {
        this.contactList = contactList;
        this.screenHeight = screenHeight;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        TagModel tag = contactList.get(position);
        holder.tagName.setText(tag.getName());
        holder.timeName.setText(tag.getTime());
        holder.itemView.getLayoutParams().height = screenHeight / 20;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

}

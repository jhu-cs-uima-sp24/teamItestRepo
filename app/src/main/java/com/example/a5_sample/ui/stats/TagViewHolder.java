package com.example.a5_sample.ui.stats;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.a5_sample.R;

public class TagViewHolder extends RecyclerView.ViewHolder {

    public TextView tagName;
    public TextView timeName;


    public TagViewHolder(View itemView) {
        super(itemView);
        tagName = itemView.findViewById(R.id.tag_name);
        timeName = itemView.findViewById(R.id.tag_time);

    }
}

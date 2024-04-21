package com.example.LifePal.ui.stats;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;

        if (includeEdge) {
            // Adjust spacing to include edges
            outRect.left = spacing - column * spacing / spanCount; // Decrease left offset as column number increases
            outRect.right = (column + 1) * spacing / spanCount;    // Increase right offset as column number increases

            // Since you do not want to change vertical spacing, ensure these are consistent or zero
            outRect.top = 0; // Set to 0 or maintain existing value if not adjusting vertical spacing at top
            outRect.bottom = 0; // Set to 0 to avoid changing vertical spacing at bottom

            // Optionally, you might want to apply spacing to the top of the first row
            if (position < spanCount) {
                outRect.top = spacing; // Apply top spacing only for the first row if needed
            }
        } else {
            // Adjust spacing without including edges
            outRect.left = column * spacing / spanCount; // Increase left offset as column number increases
            outRect.right = spacing - (column + 1) * spacing / spanCount; // Decrease right offset as column number increases

            // Since you do not want to change vertical spacing, ensure these are consistent or zero
            outRect.top = 0; // Set to 0 or maintain existing value if not adjusting vertical spacing at top
            outRect.bottom = 0; // Set to 0 to avoid changing vertical spacing at bottom

            // Optionally, you might want to apply spacing above items not in the first row
            if (position >= spanCount) {
                outRect.top = spacing; // Apply top spacing only for items not in the first row if needed
            }
        }
    }

}
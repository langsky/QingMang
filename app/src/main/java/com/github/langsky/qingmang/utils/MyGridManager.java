package com.github.langsky.qingmang.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;

/**
 * to resolve an exception
 * Created by swd1 on 17-1-22.
 */

public class MyGridManager extends StaggeredGridLayoutManager {

    private static final String TAG = "MyStaggeredGridLayout";

    public MyGridManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyGridManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public void collectAdjacentPrefetchPositions(int dx, int dy, RecyclerView.State state, LayoutPrefetchRegistry layoutPrefetchRegistry) {
       try {super.collectAdjacentPrefetchPositions(dx, dy, state, layoutPrefetchRegistry);}
       catch (Exception e){
           Log.e(TAG, "collectAdjacentPrefetchPositions: ");
       }
    }
}

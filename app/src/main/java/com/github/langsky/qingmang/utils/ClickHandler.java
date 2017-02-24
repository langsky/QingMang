package com.github.langsky.qingmang.utils;

import android.support.v7.widget.RecyclerView;

/**
 * Created by swd1 on 17-1-19.
 */

public interface ClickHandler<V extends RecyclerView.ViewHolder> {

    void onClick(V view, int position);

    boolean onLongClick(V view, int position);

    void onChildClick(V view, int position, int childIndex);

    void pureClick(int index);

}

package com.github.langsky.qingmang.utils;

import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by swd1 on 17-1-25.
 */

public class BaseClickHandler<V extends ViewHolder> implements ClickHandler<V> {

    @Override
    public void onClick(V view, int position) {

    }

    @Override
    public boolean onLongClick(V view, int position) {
        return false;
    }

    @Override
    public void onChildClick(V view, int position, int childIndex) {

    }

    @Override
    public void pureClick(int index) {

    }
}

package com.github.langsky.qingmang.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by swd1 on 17-2-23.
 * abstract adapter that has header and footer which can be clicked.
 */

public abstract class HasHeaderFooterAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{
    public abstract boolean isHeaderOrFooter(int position);
}

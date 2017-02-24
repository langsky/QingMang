package com.github.langsky.qingmang.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.github.langsky.qingmang.ArticleListActivity;
import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.databinding.FooterHistoryBinding;
import com.github.langsky.qingmang.databinding.HeaderHistoryBinding;
import com.github.langsky.qingmang.databinding.ItemMagazineBinding;
import com.github.langsky.qingmang.databinding.TitleAppBinding;
import com.github.langsky.qingmang.event.RxBus;
import com.github.langsky.qingmang.event.StringEvent;
import com.github.langsky.qingmang.mvp.model.Magazine;
import com.github.langsky.qingmang.utils.BaseClickHandler;
import com.github.langsky.qingmang.utils.C;
import com.github.langsky.qingmang.utils.ColorUtils;
import com.github.langsky.qingmang.utils.StoreUtils;
import com.github.langsky.qingmang.utils.UIUtils;

import java.util.List;


/**
 * @author langsky
 * @Title: MagazinesAdapter
 * @Description: An adapter class to load magazines.
 * @date 2016-12-02
 * @email guoliang.hu@hotmail.com
 */
public class MagazinesHistoryAdapter extends HasHeaderFooterAdapter<MagazinesHistoryAdapter.ViewHolder> {

    private static final String TAG = "MagazinesHistoryAdapter";

    private static final int TYPE_TITLE = -1;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private Context context;
    private List<Magazine> magazines;

    private final ColorUtils colorUtils;
    private final MyClickHandler clickHandler;

    public MagazinesHistoryAdapter(Context context, ColorUtils colorUtils) {
        this.context = context;
        this.colorUtils = colorUtils;
        this.clickHandler = new MyClickHandler();
    }

    public int initDataSet() {
        if (magazines != null)
            magazines.clear();

        magazines = StoreUtils.getAllHistoryMagazines();

        int size = magazines.size();

        if (size==0){
            RxBus.instance().post(new StringEvent(StringEvent.EVENT_MAGAZINE_HISTORY_LIST_EMPTY));
        }else {
            notifyDataSetChanged();
        }

        return size;
    }

    private void removeAll() {
        StoreUtils.removeAllHistoryMagazine();
        notifyItemRangeRemoved(2, magazines.size());
        magazines.clear();
        RxBus.instance().post(new StringEvent(StringEvent.EVENT_MAGAZINE_HISTORY_LIST_EMPTY));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_TITLE:
                view = TitleAppBinding.inflate(LayoutInflater.from(context), parent, false).getRoot();
                return new ViewHolder(view, TYPE_TITLE);
            case TYPE_HEADER:
                view = HeaderHistoryBinding.inflate(LayoutInflater.from(context), parent, false).getRoot();
                return new ViewHolder(view, TYPE_HEADER);
            case TYPE_ITEM:
                view = ItemMagazineBinding.inflate(LayoutInflater.from(context), parent, false).getRoot();
                return new ViewHolder(view, TYPE_ITEM);
            case TYPE_FOOTER:
                view = FooterHistoryBinding.inflate(LayoutInflater.from(context), parent, false).getRoot();
                return new ViewHolder(view, TYPE_FOOTER);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position == 0) {
            holder.appBinding.setTitle(context.getString(R.string.history_magazine));
            holder.appBinding.setColorUtils(colorUtils);
            holder.appBinding.setClickHandler(clickHandler);
            holder.appBinding.transformHistory.setImageResource(R.drawable.ic_book_white_24dp);
            holder.appBinding.transformHistory.setVisibility(View.VISIBLE);
            return;
        }
        if (position == 1) {
            holder.headerBinding.setTitle(context.getString(R.string.reading_history));
            holder.headerBinding.setColorUtils(colorUtils);
            holder.headerBinding.setViewHolder(holder);
            holder.headerBinding.setClickHandler(clickHandler);
            return;
        }
        if (position == getItemCount() - 1) {
            holder.footerBinding.setTitle(context.getString(R.string.delete_all_history));
            holder.footerBinding.setColorUtils(colorUtils);
            holder.footerBinding.setViewHolder(holder);
            holder.footerBinding.setClickHandler(clickHandler);
            return;
        }

        holder.itemBinding.setViewHolder(holder);
        holder.itemBinding.setClickHandler(clickHandler);
        holder.itemBinding.item.setUiData(magazines.get(position - 2));

    }

    @Override
    public int getItemCount() {
        return magazines.size() + 3;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        int viewType;
        View itemView;
        TitleAppBinding appBinding;
        ItemMagazineBinding itemBinding;
        HeaderHistoryBinding headerBinding;
        FooterHistoryBinding footerBinding;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            this.itemView = itemView;
            switch (viewType) {
                case TYPE_TITLE:
                    appBinding = DataBindingUtil.bind(itemView);
                    break;
                case TYPE_HEADER:
                    headerBinding = DataBindingUtil.bind(itemView);
                    break;
                case TYPE_ITEM:
                    itemBinding = DataBindingUtil.bind(itemView);
                    break;
                case TYPE_FOOTER:
                    footerBinding = DataBindingUtil.bind(itemView);
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_TITLE : position == 1 ? TYPE_HEADER : position == getItemCount() - 1 ? TYPE_FOOTER : TYPE_ITEM;
    }

    public boolean isHeaderOrFooter(int position) {
        return position == 0 || position == 1 || position == getItemCount() - 1;
    }

    //专门处理点击事件
    private class MyClickHandler extends BaseClickHandler<ViewHolder> {


        @Override
        public void onClick(ViewHolder view, int position) {
            switch (view.viewType) {
                case TYPE_HEADER:
                    Log.i(TAG, "onClick: HEADER");
                    break;
                case TYPE_FOOTER:
                    Log.i(TAG, "onClick: FOOTER");
                    removeAll();
                    break;
                case TYPE_ITEM:
                    UIUtils.startArticleActivity(context, magazines.get(position - 2));
                    break;
            }
        }

        @Override
        public void pureClick(int index) {
            switch (index) {
                case C.BTN_TRANSFORM_HISTORY:
                    RxBus.instance().post(new StringEvent(StringEvent.EVENT_MAGAZINE_TRANSFORM_HISTORY));
                    break;
            }
        }
    }

}

package com.github.langsky.qingmang.adapter;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.github.langsky.qingmang.ArticleListActivity;
import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.databinding.FooterMagazineBinding;
import com.github.langsky.qingmang.databinding.HeaderMagazineBinding;
import com.github.langsky.qingmang.databinding.ItemMagazineBinding;
import com.github.langsky.qingmang.databinding.TitleAppBinding;
import com.github.langsky.qingmang.event.RxBus;
import com.github.langsky.qingmang.event.StringEvent;
import com.github.langsky.qingmang.mvp.model.Magazine;
import com.github.langsky.qingmang.mvp.model.MagazineSet;
import com.github.langsky.qingmang.utils.BaseClickHandler;
import com.github.langsky.qingmang.utils.C;
import com.github.langsky.qingmang.utils.ColorUtils;
import com.github.langsky.qingmang.utils.UIUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * @author langsky
 * @Title: MagazinesAdapter
 * @Description: An adapter class to load all magazines.
 * @date 2016-12-02
 * @email guoliang.hu@outlook.com
 */

public class MagazinesAdapter extends HasHeaderFooterAdapter<MagazinesAdapter.ViewHolder> {

    private static final String TAG = "MagazinesAdapter";

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_ITEM = -1;
    private static final int TYPE_FOOTER = 2;

    private Context context;

    private final ColorUtils colorUtils;

    private final List<MagazineSet> magazineSetList;
    private final List<Integer> headerPositions;
    private final List<Integer> footerPositions;

    private final SparseArray<Magazine> magazineMap;

    private final MyClickHandler clickHandler;


    public MagazinesAdapter(Context context, ColorUtils colorUtils) {
        this.context = context;
        this.colorUtils = colorUtils;

        this.magazineSetList = new ArrayList<>();
        this.headerPositions = new ArrayList<>();
        this.footerPositions = new ArrayList<>();

        this.magazineMap = new SparseArray<>();
        this.clickHandler = new MyClickHandler();


    }

    public void freshData(List<MagazineSet> list) {
        magazineMap.clear();
        magazineSetList.clear();
        magazineSetList.addAll(list);
        initPosition();
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_TITLE:
                view = TitleAppBinding.inflate(LayoutInflater.from(context), parent, false).getRoot();
                return new ViewHolder(view, TYPE_TITLE);
            case TYPE_HEADER:
                view = HeaderMagazineBinding.inflate(LayoutInflater.from(context), parent, false).getRoot();
                return new ViewHolder(view, TYPE_HEADER);
            case TYPE_ITEM:
                view = ItemMagazineBinding.inflate(LayoutInflater.from(context), parent, false).getRoot();
                return new ViewHolder(view, TYPE_ITEM);
            case TYPE_FOOTER:
                view = FooterMagazineBinding.inflate(LayoutInflater.from(context), parent, false).getRoot();
                return new ViewHolder(view, TYPE_FOOTER);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        if (position == 0) {
            holder.appBinding.setTitle("所有杂志");
            holder.appBinding.setColorUtils(colorUtils);
            holder.appBinding.setClickHandler(clickHandler);
            holder.appBinding.transformHistory.setImageResource(R.drawable.ic_history_white_24dp);
            holder.appBinding.transformHistory.setVisibility(View.VISIBLE);
            return;
        }

        if (headerPositions.contains(position)) {
            final int index = headerPositions.indexOf(position);
            holder.headerBinding.setTitle(magazineSetList.get(index).getTitle());
            holder.headerBinding.setColorUtils(colorUtils);
            holder.headerBinding.setViewHolder(holder);
            holder.headerBinding.setClickHandler(clickHandler);
            return;
        }

        if (footerPositions.contains(position)) {
            final int index = footerPositions.indexOf(position);
            holder.footerBinding.setTitle(magazineSetList.get(index).getTitle());
            holder.footerBinding.setColorUtils(colorUtils);
            holder.footerBinding.setViewHolder(holder);
            holder.footerBinding.setClickHandler(clickHandler);
            return;
        }

        holder.itemBinding.setViewHolder(holder);
        holder.itemBinding.setClickHandler(clickHandler);
        holder.itemBinding.item.setUiData(magazineMap.get(position));
    }


    @Override
    public int getItemCount() {
        return footerPositions.size() + headerPositions.size() + magazineMap.size() + 1;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        int viewType;
        View itemView;
        TitleAppBinding appBinding;
        ItemMagazineBinding itemBinding;
        HeaderMagazineBinding headerBinding;
        FooterMagazineBinding footerBinding;

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
        return position == 0 ? TYPE_TITLE : headerPositions.contains(position) ? TYPE_HEADER :
                footerPositions.contains(position) ? TYPE_FOOTER : TYPE_ITEM;
    }

    /**
     * whether the position is on foot or head.
     *
     * @param position position integer value.
     * @return boolean
     */
    public boolean isHeaderOrFooter(int position) {
        return position == 0 || footerPositions.contains(position) || headerPositions.contains(position);
    }

    private void initPosition() {
        int temp1 = 1;
        int temp2 = 0;
        int temp3 = -1;
        for (MagazineSet ms :
                magazineSetList) {
            headerPositions.add(temp1);
            temp1 += ms.getMagazines().size() + 2;
            temp2 += ms.getMagazines().size() + 2;
            temp3 += 2;
            footerPositions.add(temp2);
            for (int i = 0; i < ms.getMagazines().size(); i++) {
                temp3 += 1;
                magazineMap.put(temp3, ms.getMagazines().get(i));
            }
        }
    }


    //专门处理点击事件
    private class MyClickHandler extends BaseClickHandler<ViewHolder> {

        @Override
        public void onClick(ViewHolder view, int position) {
            switch (view.viewType) {
                case TYPE_TITLE:
                    break;
                case TYPE_HEADER:
                    Toast.makeText(context, magazineSetList.get(headerPositions.indexOf(position)).getTitle(), Toast.LENGTH_SHORT).show();
                    break;
                case TYPE_FOOTER:
                    Toast.makeText(context, magazineSetList.get(footerPositions.indexOf(position)).getTitle(), Toast.LENGTH_SHORT).show();
                    break;
                case TYPE_ITEM:
                    UIUtils.startArticleActivity(context, magazineMap.get(position));
                    break;
            }
        }

        @Override
        public void pureClick(int index) {
            switch (index) {
                case C.BTN_TRANSFORM_HISTORY:
                    RxBus.instance().post(new StringEvent(StringEvent.EVENT_MAGAZINE_TRANSFORM_HISTORY));
            }
        }
    }

}

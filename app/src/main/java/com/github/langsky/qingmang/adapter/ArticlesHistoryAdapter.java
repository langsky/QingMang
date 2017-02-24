package com.github.langsky.qingmang.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.databinding.FooterHistoryBinding;
import com.github.langsky.qingmang.databinding.HeaderHistoryBinding;
import com.github.langsky.qingmang.databinding.ItemArticleHistoryBinding;
import com.github.langsky.qingmang.databinding.TitleAppBinding;
import com.github.langsky.qingmang.event.RxBus;
import com.github.langsky.qingmang.event.StringEvent;
import com.github.langsky.qingmang.mvp.model.Article;
import com.github.langsky.qingmang.utils.BaseClickHandler;
import com.github.langsky.qingmang.utils.C;
import com.github.langsky.qingmang.utils.ColorUtils;
import com.github.langsky.qingmang.utils.StoreUtils;
import com.github.langsky.qingmang.utils.UIUtils;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.List;


/**
 * @author langsky
 * @Title: ArticlesAdapter
 * @Description: An adapter class to load articles.
 * @date 2016-12-02
 * @email guoliang.hu@outlook.com
 */
public class ArticlesHistoryAdapter extends RecyclerView.Adapter<ArticlesHistoryAdapter.ViewHolder> {

    private static final String TAG = "ArticlesHistoryAdapter";

    private static final int TYPE_TITLE = -1;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private Context context;
    private List<Article> articles;
    private ViewHolder currentHolder;

    private final ColorUtils colorUtils;
    private final MyClickHandler clickHandler;

    public ArticlesHistoryAdapter(Context context, ColorUtils colorUtils) {
        this.context = context;
        this.colorUtils = colorUtils;
        this.clickHandler = new MyClickHandler();
    }

    public void initDataSet() {
        if (articles != null)
            articles.clear();

        articles = StoreUtils.getAllHistoryArticles();

        for (int i = 0; i < articles.size(); i++) {
            Log.e(TAG, "initDataSet: " + articles.get(i).getTitle() + '\n');
        }

        if (articles.size() == 0)
            RxBus.instance().post(new StringEvent(StringEvent.EVENT_ARTICLE_HISTORY_LIST_EMPTY));
        else
            RxBus.instance().post(new StringEvent(StringEvent.EVENT_ARTICLE_HISTORY_LIST_REFRESH));

    }


    private void remove(int position) {
        Article article = articles.get(position - 2);
        StoreUtils.removeHistoryArticle(article);
        articles.remove(position - 2);
        if (articles.size() == 0) {
            RxBus.instance().post(new StringEvent(StringEvent.EVENT_ARTICLE_HISTORY_LIST_EMPTY));
            return;
        }
        notifyItemRemoved(position);
    }

    private void removeAll() {
        StoreUtils.removeAllHistoryArticle();
        //notifyItemRangeRemoved(2, articles.size());
        articles.clear();
        RxBus.instance().post(new StringEvent(StringEvent.EVENT_ARTICLE_HISTORY_LIST_EMPTY));
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
                view = ItemArticleHistoryBinding.inflate(LayoutInflater.from(context), parent, false).getRoot();
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
            holder.appBinding.setTitle(context.getString(R.string.history_header));
            holder.appBinding.setColorUtils(colorUtils);
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
        holder.itemBinding.flipCard.init(C.BTN_FLIP_DEFAULT_FRONT, C.BTN_FLIP_DEFAULT_BACK);
        holder.itemBinding.flipCard.setClickable(true);
        holder.itemBinding.item.setUiData(articles.get(position - 2));

    }

    @Override
    public int getItemCount() {
        return articles.size() + 3;
    }


    public ViewHolder getCurrentHolder() {
        return currentHolder;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        int viewType;
        View itemView;
        TitleAppBinding appBinding;
        public ItemArticleHistoryBinding itemBinding;
        HeaderHistoryBinding headerBinding;
        FooterHistoryBinding footerBinding;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            this.itemView = itemView;
            switch (viewType) {
                case TYPE_TITLE:
                    ((StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams()).setFullSpan(true);
                    appBinding = DataBindingUtil.bind(itemView);
                    break;
                case TYPE_HEADER:
                    //you must use DataBindingUtil instead of direct Binding class, or an issue will occur.
                    ((StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams()).setFullSpan(true);
                    headerBinding = DataBindingUtil.bind(itemView);
                    break;
                case TYPE_ITEM:
                    itemBinding = DataBindingUtil.bind(itemView);
                    break;
                case TYPE_FOOTER:
                    ((StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams()).setFullSpan(true);
                    footerBinding = DataBindingUtil.bind(itemView);
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_TITLE : position == 1 ? TYPE_HEADER : position == getItemCount() - 1 ? TYPE_FOOTER : TYPE_ITEM;
    }

    //专门处理点击事件
    private class MyClickHandler extends BaseClickHandler<ViewHolder> {


        @Override
        public void onClick(ViewHolder view, int position) {
            switch (view.viewType) {
                case TYPE_HEADER:
                    Log.v(TAG, "onClick: HEADER");
                    break;
                case TYPE_FOOTER:
                    Log.v(TAG, "onClick: FOOTER");
                    removeAll();
                    break;
                case TYPE_ITEM:
                    Log.i(TAG, "onClick: ITEM");
                    new FinestWebView.Builder(context).showIconMenu(false).show(articles.get(position - 2).getRealUrl());
                    break;
            }
        }

        @Override
        public void onChildClick(ViewHolder view, int position, int childIndex) {
            if (childIndex == C.BTN_SHARE) {
                UIUtils.shareArticle(context, null);
            }
            if (childIndex == C.BTN_DELETE) {
                remove(view.getLayoutPosition());
            }

            if (childIndex == C.BTN_FLIP_DEFAULT_BACK) {
                if (currentHolder != null && currentHolder.itemBinding.flipCard.isBack())
                    currentHolder.itemBinding.flipCard.flipCard();
                view.itemBinding.flipCard.flipCard();
                currentHolder = view;
            }

            if (childIndex == C.BTN_FLIP_DEFAULT_FRONT) {
                view.itemBinding.flipCard.flipCard();
                currentHolder = null;
            }
        }
    }

}

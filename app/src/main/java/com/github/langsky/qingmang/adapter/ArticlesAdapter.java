package com.github.langsky.qingmang.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.langsky.qingmang.databinding.FooterArticleBinding;
import com.github.langsky.qingmang.databinding.HeaderArticleBinding;
import com.github.langsky.qingmang.databinding.ItemArticleBinding;
import com.github.langsky.qingmang.mvp.model.ArticleSet;
import com.github.langsky.qingmang.utils.BaseClickHandler;
import com.github.langsky.qingmang.utils.C;
import com.github.langsky.qingmang.utils.UIUtils;


/**
 * @author langsky
 * @title: ArticlesAdapter
 * @description: An adapter class to load articles.
 * @date 2016-12-02
 * @email guoliang.hu@outlook.com
 */
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private static final String TAG = "ArticlesAdapter";

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    private Context context;
    private boolean hasNextPage; //whether has next page.
    private ArticleSet articleSet; //current article set.
    private ViewHolder currentHolder; //current ViewHolder.

    private final String title;
    private final MyClickHandler clickHandler;

    public ArticlesAdapter(Context context, String title) {
        this.title = title;
        this.context = context;
        this.clickHandler = new MyClickHandler();
        this.articleSet = new ArticleSet();
    }

    public void setArticleSet(ArticleSet set) {
        articleSet = set;
        hasNextPage = !articleSet.getNextUrl().contains("#");
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_HEADER:
                view = HeaderArticleBinding.inflate(LayoutInflater.from(context), parent, false).getRoot();
                return new ViewHolder(view, TYPE_HEADER);
            case TYPE_ITEM:
                view = ItemArticleBinding.inflate(LayoutInflater.from(context), parent, false).getRoot();
                return new ViewHolder(view, TYPE_ITEM);
            case TYPE_FOOTER:
                view = FooterArticleBinding.inflate(LayoutInflater.from(context), parent, false).getRoot();
                return new ViewHolder(view, TYPE_FOOTER);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position == 0) {
            holder.headerBinding.setTitle(title);
            holder.headerBinding.setViewHolder(holder);
            holder.headerBinding.setClickHandler(clickHandler);
            return;
        }

        if (position == getItemCount() - 1) {
            holder.footerBinding.setHasNextPage(hasNextPage);
            holder.footerBinding.setViewHolder(holder);
            holder.footerBinding.setArticleSet(articleSet);
            holder.footerBinding.setClickHandler(clickHandler);
            return;
        }

        holder.itemBinding.flipCard.init(C.BTN_FLIP_DEFAULT_FRONT, C.BTN_FLIP_DEFAULT_BACK);
        holder.itemBinding.setViewHolder(holder);
        holder.itemBinding.setClickHandler(clickHandler);
        holder.itemBinding.itemArticle.setUiData(articleSet.getArticles().get(position - 1));
    }

    @Override
    public int getItemCount() {
        return articleSet.getArticles().size() + 2;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        int viewType;
        public ItemArticleBinding itemBinding;
        public HeaderArticleBinding headerBinding;
        public FooterArticleBinding footerBinding;

        ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            switch (viewType) {
                case TYPE_HEADER:
                    //you must use DataBindingUtil instead of direct Binding class, or an issue will occur.
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

    public ViewHolder getCurrentHolder() {
        return currentHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : position == getItemCount() - 1 ? TYPE_FOOTER : TYPE_ITEM;
    }

    //click handler
    private class MyClickHandler extends BaseClickHandler<ViewHolder> {

        @Override
        public void onClick(ViewHolder view, int position) {
            switch (view.viewType) {
                case TYPE_HEADER:
                    UIUtils.onClickArticleHeader(context, view);
                    Log.e(TAG, "onClick: HEADER");
                    break;
                case TYPE_FOOTER:
                    UIUtils.onClickArticleFooter(context, view);
                    Log.e(TAG, "onClick: FOOTER");
                    break;
                case TYPE_ITEM:
                    UIUtils.onClickArticleItem(articleSet.getArticles().get(position - 1));
                    Log.e(TAG, "onClick: ITEM");
                    break;
            }
        }

        @Override
        public void onChildClick(ViewHolder view, int position, int childIndex) {

            if (childIndex == C.BTN_SHARE) {
                UIUtils.shareArticle(context, null);
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

            //footer click
            if (childIndex == C.BTN_BEFORE) {
                UIUtils.onClickArticleSet(articleSet.getPrevUrl());
            }

            if (childIndex == C.BTN_AFTER) {
                UIUtils.onClickArticleSet(articleSet.getNextUrl());
            }
        }

    }

}

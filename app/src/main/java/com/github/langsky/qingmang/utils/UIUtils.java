package com.github.langsky.qingmang.utils;


import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.os.SystemClock;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.github.langsky.qingmang.ArticleListActivity;
import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.adapter.ArticlesAdapter;
import com.github.langsky.qingmang.adapter.ArticlesHistoryAdapter;
import com.github.langsky.qingmang.event.ArticleEvent;
import com.github.langsky.qingmang.event.ArticleSetEvent;
import com.github.langsky.qingmang.event.RxBus;
import com.github.langsky.qingmang.mvp.model.Article;
import com.github.langsky.qingmang.mvp.model.Magazine;
import com.github.langsky.qingmang.widget.layout.ArticleViewHandler;
import com.thefinestartist.finestwebview.FinestWebView;


/**
 * UI event handler Created by swd1 on 16-12-31.
 */

public class UIUtils {

    private static final String TAG = "UIUtils";

    public static void onClickArticleHeader(Context context, ArticlesAdapter.ViewHolder viewHolder) {
        Toast.makeText(context, viewHolder.headerBinding.getTitle(), Toast.LENGTH_SHORT).show();
    }

    public static void onClickArticleFooter(Context context, ArticlesAdapter.ViewHolder viewHolder) {
        // TODO: 17-1-25  add operation
    }

    public static void onClickArticleItem(Article article) {
        Log.e(TAG, "onClickArticleItem: " + article.getUrl());
        article.setCurrentTime(SystemClock.currentThreadTimeMillis());
        RxBus.instance().post(new ArticleEvent(article, true));
    }

    public static void startArticleActivity(Context context, Magazine magazine) {

        Intent intent = new Intent(context, ArticleListActivity.class);
        intent.putExtra("URL", magazine.getUrl());
        intent.putExtra("title", magazine.getTitle());

        StoreUtils.setMagazineHistoric(magazine);

        context.startActivity(intent);
    }


    public static void onClickArticleSet(String url) {
        RxBus.instance().post(new ArticleSetEvent(url));
    }

    public static void goGitHub(Context context) {
        final String web = "https://github.com/langsky";
        new FinestWebView.Builder(context).showIconMenu(false).showIconMenu(false).show(web);
    }

    public static void goWordpress(Context context) {
        final String web = "https://0901blog.wordpress.com";
        new FinestWebView.Builder(context).showIconMenu(false).showIconMenu(false).show(web);
    }

    public static void shareArticle(Context context, Article article) {
        Toast.makeText(context, "分享功能还没做呢，请关注我的GitHub，我会不定时更新", Toast.LENGTH_SHORT).show();
    }

    @BindingAdapter("bind:imageUrl")
    public static void imageUrl(ImageView view, final String url) {
        if (TextUtils.isEmpty(url) || Config.UI_FLAGS.contains(ArticleViewHandler.UiFlags.NO_IMAGE_LOADING)) {
            view.setVisibility(View.GONE);
            return;
        }

        Glide.with(view.getContext())
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.article_holder)
                .skipMemoryCache(false)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(view);
    }

    @BindingAdapter("bind:coverUrl")
    public static void coverUrl(ImageView view, final String url) {
        if (Config.UI_FLAGS.contains(ArticleViewHandler.UiFlags.NO_IMAGE_LOADING)) {
            view.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(url)) {
            Glide.with(view.getContext())
                    .load(R.drawable.magazine_holder)
                    .centerCrop()
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(view);
            return;
        }

        Glide.with(view.getContext())
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.magazine_holder)
                .centerCrop()
                .thumbnail(0.8f)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new ImgViewTarget(view, ImageView.ScaleType.CENTER_CROP));
    }

    @BindingAdapter("bind:photoUrl")
    public static void photoUrl(ImageView view, final String url) {
        if (Config.UI_FLAGS.contains(ArticleViewHandler.UiFlags.NO_IMAGE_LOADING)) {
            view.setVisibility(View.GONE);
            return;
        }
        Glide.with(view.getContext())
                .load(url)
                .centerCrop()
                .skipMemoryCache(false)
                .thumbnail(0.8f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(view);
    }

}






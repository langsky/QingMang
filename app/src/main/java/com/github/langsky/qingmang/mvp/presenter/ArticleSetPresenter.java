package com.github.langsky.qingmang.mvp.presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.langsky.qingmang.adapter.ArticlesAdapter;
import com.github.langsky.qingmang.databinding.RecyclerArticlesBinding;
import com.github.langsky.qingmang.event.ArticleEvent;
import com.github.langsky.qingmang.event.ArticleSetEvent;
import com.github.langsky.qingmang.event.RxBus;
import com.github.langsky.qingmang.event.StringEvent;
import com.github.langsky.qingmang.mvp.model.Article;
import com.github.langsky.qingmang.mvp.model.ArticleSet;
import com.github.langsky.qingmang.mvp.presenter.abs.NetPresenter;
import com.github.langsky.qingmang.mvp.view.abs.IView;
import com.github.langsky.qingmang.network.Api;
import com.github.langsky.qingmang.utils.StoreUtils;
import com.thefinestartist.finestwebview.FinestWebView;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by swd1 on 17-1-9.
 */

public class ArticleSetPresenter implements NetPresenter {

    private static final String TAG = "ArticleSetPresenter";

    private IView view;
    private Context context;

    private String title;
    private String url;

    private LinearLayoutManager manager;
    private RecyclerArticlesBinding model;
    private Subscription subscription1;
    private Subscription subscription2;

    private ArticlesAdapter adapter;

    public ArticleSetPresenter(Context context) {
        this.context = context;
        manager = new LinearLayoutManager(context);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void registerNetData(String url) {
        this.url = url;
        Api.getArticles(url).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleSet>() {
                    @Override
                    public void onStart() {
                        view.begin(true);
                    }

                    @Override
                    public void onCompleted() {
                        view.completed(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.error(e.getMessage());
                    }

                    @Override
                    public void onNext(ArticleSet set) {
                        adapter = new ArticlesAdapter(context, title);
                        adapter.setArticleSet(set);
                        model.viewItems.setAdapter(adapter);
                    }
                });
    }


    @Override
    public void refreshData() {
        registerNetData(url);
    }


    @Override
    public void registerRxBus() {
        subscription1 = RxBus.instance().toObservable(ArticleEvent.class).subscribe(new Action1<ArticleEvent>() {
            @Override
            public void call(ArticleEvent articleEvent) {
                parseUrl(articleEvent.getArticle());
            }
        });

        subscription2 = RxBus.instance().toObservable(ArticleSetEvent.class).subscribe(new Subscriber<ArticleSetEvent>() {
            @Override
            public void onCompleted() {
                view.completed(false);
            }

            @Override
            public void onError(Throwable e) {
                view.error(e.getMessage());
            }

            @Override
            public void onNext(ArticleSetEvent articleSetEvent) {
                view.begin(false);
                registerNetData(articleSetEvent.getUrl());
            }
        });
    }

    private void parseUrl(final Article article) {
        Api.getRealUrl(article.getUrl()).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                StoreUtils.setArticleHistoric(article, s);
                return s;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<String>() {
            @Override
            public void onStart() {
                view.begin(false);
            }

            @Override
            public void onCompleted() {
                view.completed(false);
            }

            @Override
            public void onError(Throwable e) {
                view.error(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                StoreUtils.setArticleHistoric(article, s);
                new FinestWebView.Builder(context).showIconMenu(false).showIconMenu(false).show(s);
            }
        });
    }


    @Override
    public void unregisterRxBus() {
        if (!subscription1.isUnsubscribed())
            subscription1.unsubscribe();
        if (!subscription2.isUnsubscribed())
            subscription2.unsubscribe();
    }

    @Override
    public void bindViewModel(IView iView) {
        this.view = iView;
        model = (RecyclerArticlesBinding) iView.getBinding();
        model.viewItems.setLayoutManager(manager);
        model.viewItems.addOnScrollListener(listener);
    }


    private RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING && adapter.getCurrentHolder() != null) {
                adapter.getCurrentHolder().itemBinding.flipCard.flipCardWithJudge();
            }
        }
    };

}

package com.github.langsky.qingmang.mvp.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.github.langsky.qingmang.adapter.ArticlesHistoryAdapter;
import com.github.langsky.qingmang.databinding.RecyclerHistoryBinding;
import com.github.langsky.qingmang.event.RxBus;
import com.github.langsky.qingmang.event.StringEvent;
import com.github.langsky.qingmang.mvp.presenter.abs.LocalPresenter;
import com.github.langsky.qingmang.mvp.view.abs.IView;
import com.github.langsky.qingmang.utils.ColorUtils;
import com.github.langsky.qingmang.utils.MyGridManager;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by swd1 on 17-1-9.
 */

public class ArticleHistoryPresenter implements LocalPresenter {

    private static final String TAG = "ArticleHistoryPresenter";

    private final ArticlesHistoryAdapter adapter;

    private Subscription subscription;

    private IView view;

    public ArticleHistoryPresenter(Context context, ColorUtils colorUtils) {
        adapter = new ArticlesHistoryAdapter(context, colorUtils);
        //registerDataInit();
    }

    @Override
    public void registerDataInit() {
        adapter.initDataSet();
    }

    @Override
    public void registerRxBus() {
        subscription = RxBus.instance().toObservable(StringEvent.class).subscribe(new Action1<StringEvent>() {
            @Override
            public void call(StringEvent stringEvent) {
                if (StringEvent.EVENT_VIEWPAGER_SCROLL.equals(stringEvent.getMessage()) && adapter.getCurrentHolder() != null) {
                    adapter.getCurrentHolder().itemBinding.flipCard.flipCardWithJudge();
                }
                if (StringEvent.EVENT_ARTICLE_HISTORY_LIST_EMPTY.equals(stringEvent.getMessage()) && view != null) {
                    view.error("no data");
                }
                if (StringEvent.EVENT_ARTICLE_HISTORY_LIST_REFRESH.equals(stringEvent.getMessage()) && view != null) {
                    view.completed(false);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void unregisterRxBus() {
        if (!subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    @Override
    public void bindViewModel(IView iView) {
        this.view = iView;

        RecyclerHistoryBinding model = (RecyclerHistoryBinding) iView.getBinding();
        StaggeredGridLayoutManager manager = new MyGridManager(2, StaggeredGridLayoutManager.VERTICAL);
        model.viewItems.setLayoutManager(manager);
        model.viewItems.setAdapter(adapter);
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

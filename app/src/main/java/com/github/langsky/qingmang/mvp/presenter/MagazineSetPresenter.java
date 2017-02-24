package com.github.langsky.qingmang.mvp.presenter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.adapter.HasHeaderFooterAdapter;
import com.github.langsky.qingmang.adapter.MagazinesAdapter;
import com.github.langsky.qingmang.adapter.MagazinesHistoryAdapter;
import com.github.langsky.qingmang.databinding.RecyclerHistoryBinding;
import com.github.langsky.qingmang.databinding.RecyclerMagazinesBinding;
import com.github.langsky.qingmang.event.RxBus;
import com.github.langsky.qingmang.event.StringEvent;
import com.github.langsky.qingmang.mvp.model.MagazineSet;
import com.github.langsky.qingmang.mvp.presenter.abs.LocalPresenter;
import com.github.langsky.qingmang.mvp.presenter.abs.NetPresenter;
import com.github.langsky.qingmang.mvp.view.abs.IView;
import com.github.langsky.qingmang.network.Api;
import com.github.langsky.qingmang.utils.BaseClickHandler;
import com.github.langsky.qingmang.utils.C;
import com.github.langsky.qingmang.utils.ColorUtils;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import org.stringtemplate.v4.ST;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by swd1 on 17-1-9.
 */

public class MagazineSetPresenter implements NetPresenter, LocalPresenter {

    private static final String TAG = "MagazineSetPresenter";

    private IView view;
    private String url;

    private boolean isHistory;
    private Context context;


    private final MagazinesAdapter adapter;
    private final MagazinesHistoryAdapter historyAdapter;

    private AVLoadingIndicatorView freshView;
    private Subscription subscription;

    public MagazineSetPresenter(Context context, ColorUtils colorUtils) {
        this.context = context;
        this.adapter = new MagazinesAdapter(context, colorUtils);
        this.historyAdapter = new MagazinesHistoryAdapter(context, colorUtils);
    }

    @Override
    public void registerDataInit() {
        view.begin(true);
        int size = historyAdapter.initDataSet();
        if (size != 0)
            view.completed(true);
    }

    @Override
    public void registerNetData(String url) {
        this.url = url;
        Api.getMagazines(url).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<MagazineSet>>() {

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
                        view.error(e.getClass().getSimpleName());
                    }

                    @Override
                    public void onNext(List<MagazineSet> magazineSets) {
                        adapter.freshData(magazineSets);
                    }
                });
    }

    @Override
    public void refreshData() {
        if (freshView != null){
            //随机的指示器
        }
        registerNetData(url);
    }

    @Override
    public void registerRxBus() {
        subscription = RxBus.instance().toObservable(StringEvent.class).subscribe(new Action1<StringEvent>() {
            @Override
            public void call(StringEvent stringEvent) {
                if (stringEvent.getMessage().equals(StringEvent.EVENT_MAGAZINE_HISTORY_LIST_EMPTY) && view != null)
                    view.error(StringEvent.EVENT_MAGAZINE_HISTORY_LIST_EMPTY);
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
        if (isHistory()) {
            GridLayoutManager manager = new GridLayoutManager(context, 2);
            RecyclerHistoryBinding model = (RecyclerHistoryBinding) iView.getBinding();
            model.setClickHandler(new BaseClickHandler() {
                @Override
                public void pureClick(int index) {
                    if (index == C.BTN_TRANSFORM_HISTORY)
                        RxBus.instance().post(new StringEvent(StringEvent.EVENT_MAGAZINE_TRANSFORM_HISTORY));
                }
            });
            setAdapterAndLayoutManager(model.viewItems, historyAdapter, manager);
        } else {
            GridLayoutManager manager = new GridLayoutManager(context, 2);
            RecyclerMagazinesBinding model = (RecyclerMagazinesBinding) iView.getBinding();

            freshView = model.fresh;

            model.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    registerNetData(C.BASE_URL);
                }
            });
            setAdapterAndLayoutManager(model.viewItems, adapter, manager);
        }
    }

    public boolean isHistory() {
        return isHistory;
    }

    public void transformHistory() {
        isHistory = !isHistory;
    }

    private void setAdapterAndLayoutManager(RecyclerView view, final HasHeaderFooterAdapter adapter, GridLayoutManager manager) {
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeaderOrFooter(position) ? 2 : 1;
            }
        });

        view.setAdapter(adapter);
        view.setLayoutManager(manager);
    }
}

package com.github.langsky.qingmang.mvp.presenter;

import android.app.Activity;
import android.databinding.DataBindingUtil;

import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.adapter.MainPagerAdapter;
import com.github.langsky.qingmang.databinding.ActivityMainBinding;
import com.github.langsky.qingmang.event.RxBus;
import com.github.langsky.qingmang.event.StringEvent;
import com.github.langsky.qingmang.utils.ColorUtils;
import com.github.langsky.qingmang.utils.NavUtils;

import java.lang.ref.WeakReference;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by swd1 on 17-2-23.
 */

public class MainPagePresenter {

    private WeakReference<Activity> activityWeakReference;
    private MainPagerAdapter adapter;

    private Subscription subscription;
    private ActivityMainBinding binding;


    public MainPagePresenter(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    public void init() {

        Activity activity = activityWeakReference.get();

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_main);
        ColorUtils colorUtils = new ColorUtils(activity.getWindow(), binding.mainViewpager);
        colorUtils.setDecorDefaultBackground();
        colorUtils.colorFollowViewpager();

        binding.setColorUtils(colorUtils);
        adapter = new MainPagerAdapter(activity, colorUtils);
        binding.mainViewpager.setAdapter(adapter);
        NavUtils.initTabBar(activity, binding.ntb, binding.mainViewpager);

        adapter.presenter1.registerRxBus();
        adapter.presenter2.registerRxBus();
        registerRxBus();
    }

    private void registerRxBus() {
        subscription = RxBus.instance().toObservable(StringEvent.class).subscribe(new Action1<StringEvent>() {
            @Override
            public void call(StringEvent stringEvent) {
                if (stringEvent.getMessage().equals(StringEvent.EVENT_MAGAZINE_TRANSFORM_HISTORY)) {
                    adapter.presenter1.transformHistory();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void unregisterRxBus() {
        if (!subscription.isUnsubscribed())
            subscription.unsubscribe();
    }

    public void finish() {
        adapter.presenter1.unregisterRxBus();
        adapter.presenter2.unregisterRxBus();
        unregisterRxBus();
    }

    public void resumeState() {
        adapter.presenter2.registerDataInit();
    }

    public void refreshState() {
        adapter.presenter1.refreshData();
    }

}

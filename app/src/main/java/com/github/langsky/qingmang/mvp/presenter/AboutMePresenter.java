package com.github.langsky.qingmang.mvp.presenter;

import android.content.Context;
import android.util.Log;


import com.github.langsky.qingmang.utils.BaseClickHandler;
import com.github.langsky.qingmang.utils.ClickHandler;
import com.github.langsky.qingmang.databinding.AboutMeLayoutBinding;
import com.github.langsky.qingmang.mvp.presenter.abs.LocalPresenter;
import com.github.langsky.qingmang.mvp.view.abs.IView;
import com.github.langsky.qingmang.utils.C;
import com.github.langsky.qingmang.utils.ColorUtils;
import com.github.langsky.qingmang.utils.UIUtils;


/**
 * Created by swd1 on 17-1-23.
 */

public class AboutMePresenter implements LocalPresenter {

    private static final String TAG = "AboutMePresenter";

    private final ColorUtils colorUtils;

    private Context context;

    //private Subscription subscription;

    public AboutMePresenter(Context context, ColorUtils colorUtils) {
        this.context = context;
        this.colorUtils = colorUtils;
    }

    private ClickHandler clickHandler = new BaseClickHandler() {

        @Override
        public void pureClick(int index) {
            Log.i(TAG, "pureClick: " + index);
            if (index <= C.MAX_COUNT_ABOUT_ME && !binding.flipCard.isAnim()) {
                binding.flipCard.setNextBack(index);
                binding.flipCard.flipCard();
            }

            if (index == C.BTN_GITHUB) {
                UIUtils.goGitHub(context);
            }

            if (index == C.BTN_WORFDPRESS) {
                UIUtils.goWordpress(context);
            }

            /*if (index == C.BTN_IMAGE) {
                final boolean hasImage = !Config.UI_FLAGS.isEmpty();
                if (hasImage)
                    Config.UI_FLAGS.add(ArticleViewHandler.UiFlags.NO_IMAGE_LOADING);
                else
                    Config.UI_FLAGS.clear();

                RxBus.instance().post(new UiFlagEvent(!hasImage));
            }*/
        }
    };


    private AboutMeLayoutBinding binding;

    @Override
    public void registerDataInit() {
    }

    @Override
    public void bindViewModel(IView iView) {
        binding = (AboutMeLayoutBinding) iView.getBinding();
        binding.setClickHandler(clickHandler);
        binding.setColorUtils(colorUtils);
        binding.flipCard.init(C.BTN_FLIP_DEFAULT_FRONT, C.BTN_FLIP_DEFAULT_BACK);
    }

    @Override
    public void registerRxBus() {
       /* subscription = RxBus.instance().toObservable(UiFlagEvent.class).subscribe(new Action1<UiFlagEvent>() {
            @Override
            public void call(UiFlagEvent uiFlagEvent) {

            }
        });*/
    }

    @Override
    public void unregisterRxBus() {

    }

}

package com.github.langsky.qingmang.mvp.view;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.databinding.RecyclerHistoryBinding;
import com.github.langsky.qingmang.mvp.view.abs.IView;
import com.github.langsky.qingmang.utils.C;

/**
 * Created by swd1 on 17-1-18.
 */

public class ArticleHistoryView implements IView {

    private static final String TAG = "ArticleHistoryView";

    private final AnimatorSet fadeOut;

    private RecyclerHistoryBinding binding;

    public ArticleHistoryView(Context context, RecyclerHistoryBinding binding) {
        this.binding = binding;

        fadeOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.fade_out_animation);

        initAnimator();
    }

    public RecyclerHistoryBinding getBinding() {
        return binding;
    }

    public void setBinding(RecyclerHistoryBinding binding) {
        this.binding = binding;
    }

    @Override
    public void begin(boolean anim) {
        //do nothing.
    }

    @Override
    public void completed(boolean anim) {
        binding.viewItems.setAlpha(1);
        binding.viewItems.setVisibility(View.VISIBLE);
        binding.textInfo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void error(String e) {
        fadeOut.start();
        binding.textInfo.setVisibility(View.VISIBLE);
        binding.textInfo.setTypeface(C.shouJin);
        binding.textInfo.setText("当前没有阅读历史");
    }

    private void initAnimator() {

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.viewItems.setVisibility(View.INVISIBLE);
            }
        });

        fadeOut.setTarget(binding.viewItems);
    }
}

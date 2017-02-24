package com.github.langsky.qingmang.mvp.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.databinding.RecyclerArticlesBinding;
import com.github.langsky.qingmang.mvp.view.abs.IView;
import com.github.langsky.qingmang.utils.C;

/**
 * Created by swd1 on 17-1-25.
 */

public class ArticleSetView implements IView {

    private static final String TAG = "ArticleSetView";

    private final AnimatorSet fadeIn;
    private final AnimatorSet fadeOut;

    private final ValueAnimator nullAnim; //延时机制

    private RecyclerArticlesBinding binding;

    public ArticleSetView(Context context, final RecyclerArticlesBinding binding) {
        this.binding = binding;

        fadeIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.fade_in_animation);
        fadeOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.fade_out_animation);

        nullAnim = ValueAnimator.ofFloat(0, 1);
        nullAnim.setDuration(3000);

        AnimatorListenerAdapter animAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.fresh.hide();
                binding.tryAgain.setVisibility(View.VISIBLE);
                binding.textInfo.setText("重试一下吧");
                binding.textInfo.setVisibility(View.VISIBLE);
            }
        };
        nullAnim.addListener(animAdapter);

        binding.textInfo.setTypeface(C.shouJin);

        initAnimator();
    }

    public void setBinding(RecyclerArticlesBinding binding) {
        this.binding = binding;
    }

    @Override
    public void begin(boolean anim) {
        if (anim)
            fadeOut.start();
        binding.tryAgain.setVisibility(View.INVISIBLE);
        binding.textInfo.setVisibility(View.INVISIBLE);
        binding.fresh.setVisibility(View.VISIBLE);
        binding.fresh.show();
    }

    @Override
    public void completed(boolean anim) {
        if (anim)
            fadeIn.start();
        binding.viewItems.setVisibility(View.VISIBLE);
        binding.fresh.hide();
    }

    @Override
    public void error(String e) {
        fadeOut.start();
        nullAnim.start();
    }

    @Override
    public ViewDataBinding getBinding() {
        return binding;
    }


    private void initAnimator() {

        fadeIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.viewItems.setVisibility(View.VISIBLE);
            }
        });

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.viewItems.setVisibility(View.INVISIBLE);
            }
        });

        fadeIn.setTarget(binding.viewItems);
        fadeOut.setTarget(binding.viewItems);
    }
}

package com.github.langsky.qingmang.mvp.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.databinding.RecyclerMagazinesBinding;
import com.github.langsky.qingmang.mvp.view.abs.IView;
import com.github.langsky.qingmang.utils.C;

/**
 * Created by swd1 on 17-1-18.
 */

public class MagazinesView implements IView {

    private static final String TAG = "MagazinesView";

    private final AnimatorSet fadeIn;
    private final AnimatorSet fadeOut;

    private final ValueAnimator nullAnim; //延时机制

    private String errorInfo;
    private boolean isCompleted;

    private RecyclerMagazinesBinding binding;


    public MagazinesView(Context context, final RecyclerMagazinesBinding binding) {
        this.binding = binding;

        fadeIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.fade_in_animation);
        fadeOut = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.fade_out_animation);

        nullAnim = ValueAnimator.ofFloat(0, 1);
        nullAnim.setDuration(1000);

        AnimatorListenerAdapter animAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isCompleted) {
                    binding.fresh.hide();
                    binding.tryAgain.setVisibility(View.VISIBLE);
                    binding.textInfo.setText(transferErrorString(errorInfo));
                    binding.textInfo.setVisibility(View.VISIBLE);
                } else {
                    binding.viewItems.setVisibility(View.VISIBLE);
                    binding.swipeLayout.setRefreshing(false);
                    binding.fresh.hide();
                    fadeIn.start();
                }
            }
        };
        nullAnim.addListener(animAdapter);

        binding.textInfo.setTypeface(C.shouJin);

        initAnimator();
    }

    public RecyclerMagazinesBinding getBinding() {
        return binding;
    }

    public void setBinding(RecyclerMagazinesBinding binding) {
        this.binding = binding;
    }

    @Override
    public void begin(boolean anim) {
        if (anim)
        fadeOut.start();
        binding.tryAgain.setVisibility(View.INVISIBLE);
        binding.textInfo.setVisibility(View.INVISIBLE);
        binding.fresh.setVisibility(View.VISIBLE);
        binding.swipeLayout.setRefreshing(false);
        binding.fresh.show();
    }

    @Override
    public void completed(boolean anim) {
        isCompleted = true;
        nullAnim.start();
    }

    @Override
    public void error(String e) {
        isCompleted = false;
        errorInfo = e;
        fadeOut.start();
        nullAnim.start();
    }

    private String transferErrorString(String s) {
        if (s.equals("NullPointerException"))
            return "服务器抽风了，多刷新几次试试吧 :)";
        if (s.equals("TimeoutException"))
            return "连接超时了，重新刷新一下吧";
        if (s.equals("NetworkErrorException"))
            return "哎呀断网了，检查一下你的网络吧";
        else
            return "轻芒君也不知道怎么了，多尝试几次吧";
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

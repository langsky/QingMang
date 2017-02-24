package com.github.langsky.qingmang.mvp.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.databinding.RecyclerHistoryBinding;
import com.github.langsky.qingmang.event.StringEvent;
import com.github.langsky.qingmang.mvp.view.abs.IView;
import com.github.langsky.qingmang.utils.BaseClickHandler;
import com.github.langsky.qingmang.utils.C;

/**
 * Created by swd1 on 17-1-18.
 */

public class MagazineHistoryView implements IView {

    private RecyclerHistoryBinding binding;

    private Context context;

    public MagazineHistoryView(Context context, final RecyclerHistoryBinding binding) {
        this.binding = binding;
        this.context = context;
        binding.textInfo.setTypeface(C.shouJin);
        binding.transformHistory.setImageResource(R.drawable.ic_book_white_24dp);
    }

    public RecyclerHistoryBinding getBinding() {
        return binding;
    }

    public void setBinding(RecyclerHistoryBinding binding) {
        this.binding = binding;
    }

    @Override
    public void begin(boolean anim) {
        binding.viewItems.setVisibility(View.VISIBLE);
        binding.textInfo.setVisibility(View.GONE);
        binding.transformHistory.setVisibility(View.VISIBLE);
    }

    @Override
    public void completed(boolean anim) {
        binding.textInfo.setVisibility(View.GONE);
        binding.transformHistory.setVisibility(View.GONE);
        transformAnimatorIn();
    }

    @Override
    public void error(String e) {
        String errorInfo = e.equals(StringEvent.EVENT_MAGAZINE_HISTORY_LIST_EMPTY) ?
                "杂志历史阅读列表为空" : "休息一下";
        binding.viewItems.setVisibility(View.GONE);
        binding.textInfo.setVisibility(View.VISIBLE);
        binding.textInfo.setText(errorInfo);
        binding.transformHistory.setVisibility(View.VISIBLE);
    }

    public void transformAnimatorIn() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(binding.viewItems, "alpha", 0, 1);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.viewItems.setVisibility(View.VISIBLE);
            }
        });
        animator.setDuration(500);
        animator.start();
    }
}

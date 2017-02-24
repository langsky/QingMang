package com.github.langsky.qingmang.widget.layout;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;

import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.github.langsky.qingmang.R;

/**
 * Created by swd1 on 17-1-22.
 */

public class FlipCardView extends FrameLayout implements FlipCardHandler {

    private static final String TAG = "FlipCardView";

    private View front;
    private View back;

    private volatile boolean anim;

    private int currentBackIndex;
    private int currentFrontIndex;
    private int defaultFrontIndex;

    AnimatorSet right;
    AnimatorSet left;

    Context context;

    public FlipCardView(Context context) {
        this(context, null);
    }

    public FlipCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void init(int frontIndex, int backIndex) {

        defaultFrontIndex = frontIndex;

        currentFrontIndex = frontIndex;
        currentBackIndex = backIndex;

        front = getChildAt(frontIndex);
        back = getChildAt(backIndex);
        setAnimator(context);

        initView();
    }

    private void initView() {
        setCameraDistance(context);
        front.setVisibility(VISIBLE);
        back.setVisibility(INVISIBLE);
    }

    public void setAnimator(Context context) {

        right = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.right_out_animator);
        left = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.left_in_animator);

        right.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                anim = true;

                FlipCardView.this.setClickable(false);
                FlipCardView.this.back.findViewById(R.id.flip).setClickable(false);
                FlipCardView.this.front.findViewById(R.id.flip).setClickable(false);

            }
        });

        right.getChildAnimations().get(1).addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                front.setVisibility(INVISIBLE);
                back.setVisibility(VISIBLE);
            }
        });


        left.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                anim = false;
                super.onAnimationEnd(animation);
                FlipCardView.this.back.findViewById(R.id.flip).setClickable(true);
                FlipCardView.this.front.findViewById(R.id.flip).setClickable(true);

                View temp = front;
                front = back;
                back = temp;

                int i = currentBackIndex;
                currentBackIndex = currentFrontIndex;
                currentFrontIndex = i;

            }
        });
    }

    public void setCameraDistance(Context context) {
        int distance = 16000;
        float scale = context.getResources().getDisplayMetrics().density * distance;
        front.setCameraDistance(scale);
        back.setCameraDistance(scale);
    }

    @Override
    public void flipCard() {
        if (!anim) {
            left.setTarget(back);
            right.setTarget(front);
            left.start();
            right.start();
        }
    }

    public void flipCardWithJudge() {
        if (currentFrontIndex != defaultFrontIndex) {
            setNextBack(defaultFrontIndex);
            flipCard();
        }
    }

    @Override
    public void setNextBack(int childIndex) {
        if (currentFrontIndex == childIndex) {
            back = getChildAt(defaultFrontIndex);
            currentBackIndex = defaultFrontIndex;
        } else {
            back = getChildAt(childIndex);
            currentBackIndex = childIndex;
        }

        initView();
    }

    public boolean isBack() {
        return currentFrontIndex != defaultFrontIndex;
    }

    public boolean isAnim() {
        return anim;
    }
}



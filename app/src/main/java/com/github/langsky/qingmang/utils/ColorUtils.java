package com.github.langsky.qingmang.utils;

import android.animation.ArgbEvaluator;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingConversion;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.github.langsky.qingmang.QingMang;
import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.event.RxBus;
import com.github.langsky.qingmang.event.StringEvent;

import java.util.Random;


/**
 * Created by swd1 on 17-1-18.
 */

public class ColorUtils {

    private BC backgroundColor;
    private final ViewPager viewPager;
    private final Window window;

    public BC getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(BC backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public ColorUtils(Window window, ViewPager viewPager) {
        this.window = window;
        this.viewPager = viewPager;
        backgroundColor = new BC();
    }

    private static final String[] colors = QingMang.instance.getResources().getStringArray(R.array.colors);

    public void setDecorDefaultBackground() {
        window.getDecorView().setBackgroundColor(Color.parseColor(colors[1]));
    }

    public void colorFollowViewpager() {
        window.getDecorView().setSystemUiVisibility(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        final ArgbEvaluator evaluator = new ArgbEvaluator();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                backgroundColor.setColor((Integer) evaluator.evaluate(positionOffset,
                        Color.parseColor(colors[position]),
                        Color.parseColor(colors[position + 1])));
                window.setStatusBarColor(backgroundColor.getColor());
                viewPager.setBackgroundColor(backgroundColor.getColor());
            }

            @Override
            public void onPageSelected(int position) {
                window.setStatusBarColor(Color.parseColor(colors[position]));
                viewPager.setBackgroundColor(Color.parseColor(colors[position]));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING)
                    RxBus.instance().post(new StringEvent("scroll viewpager"));
            }
        });
    }

    @BindingConversion
    public static ColorDrawable getColor(int color) {
        return new ColorDrawable(color);
    }

    public static class BC extends BaseObservable {

        private int color;

        @Bindable
        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
            notifyChange();
        }
    }
}

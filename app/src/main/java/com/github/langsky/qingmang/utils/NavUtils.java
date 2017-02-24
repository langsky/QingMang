package com.github.langsky.qingmang.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;

import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.widget.navbar.NavigationTabBar;

import java.util.ArrayList;


/**
 * The class is to init tab bar.
 * Created by swd1 on 17-1-2.
 */

public class NavUtils {
    public static void initTabBar(Context context, NavigationTabBar bar, ViewPager pager) {
        final String[] colors = context.getResources().getStringArray(R.array.colors);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        context.getDrawable(R.drawable.history_white),
                        Color.parseColor(colors[0]))
                        .title("阅读历史")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        context.getDrawable(R.drawable.book_white),
                        Color.parseColor(colors[1]))
                        .title("所有杂志")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        context.getDrawable(R.drawable.plus_one_white),
                        Color.parseColor(colors[2]))
                        .title("关于我")
                        .build()
        );
        bar.setViewPager(pager, 1);
        bar.setModels(models);
    }
}

package com.github.langsky.qingmang.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.langsky.qingmang.R;
import com.github.langsky.qingmang.databinding.AboutMeLayoutBinding;
import com.github.langsky.qingmang.databinding.RecyclerHistoryBinding;
import com.github.langsky.qingmang.databinding.RecyclerMagazinesBinding;
import com.github.langsky.qingmang.mvp.presenter.AboutMePresenter;
import com.github.langsky.qingmang.mvp.presenter.ArticleHistoryPresenter;
import com.github.langsky.qingmang.mvp.presenter.MagazineSetPresenter;
import com.github.langsky.qingmang.mvp.view.AboutMeView;
import com.github.langsky.qingmang.mvp.view.ArticleHistoryView;
import com.github.langsky.qingmang.mvp.view.MagazineHistoryView;
import com.github.langsky.qingmang.mvp.view.MagazinesView;
import com.github.langsky.qingmang.utils.ColorUtils;

/**
 * Created by swd1 on 17-1-16.
 */

public class MainPagerAdapter extends PagerAdapter {

    private Context context;

    public final MagazineSetPresenter presenter1;
    public final ArticleHistoryPresenter presenter2;
    public final AboutMePresenter presenter3;


    public MainPagerAdapter(Context context, ColorUtils colorUtils) {
        this.context = context;
        presenter1 = new MagazineSetPresenter(context, colorUtils);
        presenter2 = new ArticleHistoryPresenter(context, colorUtils);
        presenter3 = new AboutMePresenter(context, colorUtils);
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        switch (position) {
            default:
            case 0:
                RecyclerHistoryBinding rfb = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.recycler_history, container, false);
                view = rfb.getRoot();
                presenter2.bindViewModel(new ArticleHistoryView(context, rfb));
                presenter2.registerDataInit();
                break;
            case 1:
                if (presenter1.isHistory()) {
                    RecyclerHistoryBinding rhb = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.recycler_history, container, false);
                    view = rhb.getRoot();
                    presenter1.bindViewModel(new MagazineHistoryView(context, rhb));
                    presenter1.registerDataInit();
                } else {
                    RecyclerMagazinesBinding rnb = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.recycler_magazines, container, false);
                    view = rnb.getRoot();
                    presenter1.bindViewModel(new MagazinesView(context, rnb));
                    presenter1.registerNetData("http://qingmang.me/magazines/");
                }
                break;
            case 2:
                AboutMeLayoutBinding amb = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.about_me_layout, container, false);
                view = amb.getRoot();
                presenter3.bindViewModel(new AboutMeView(amb));
                break;

        }
        view.setTag(position);
        container.addView(view);
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        if ((Integer) ((View) object).getTag() == 1)
            return POSITION_NONE;
        return POSITION_UNCHANGED;
    }

}

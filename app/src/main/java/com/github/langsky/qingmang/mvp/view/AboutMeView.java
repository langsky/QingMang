package com.github.langsky.qingmang.mvp.view;

import com.github.langsky.qingmang.databinding.AboutMeLayoutBinding;
import com.github.langsky.qingmang.mvp.view.abs.IView;

/**
 * Created by swd1 on 17-1-23.
 */

public class AboutMeView implements IView {

    private AboutMeLayoutBinding binding;

    public AboutMeView(AboutMeLayoutBinding binding) {
        this.binding = binding;
    }

    public AboutMeLayoutBinding getBinding() {
        return binding;
    }

    public void setBinding(AboutMeLayoutBinding binding) {
        this.binding = binding;
    }

    @Override
    public void begin(boolean anim) {

    }

    @Override
    public void completed(boolean anim) {

    }

    @Override
    public void error(String e) {

    }
}

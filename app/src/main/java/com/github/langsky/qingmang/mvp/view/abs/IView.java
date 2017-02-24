package com.github.langsky.qingmang.mvp.view.abs;

import android.databinding.ViewDataBinding;

/**
 * Created by swd1 on 16-12-30.
 */

public interface IView<M extends ViewDataBinding> {

    void begin(boolean anim);

    void completed(boolean anim);

    void error(String e);

    M getBinding();
}

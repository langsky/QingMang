package com.github.langsky.qingmang.mvp.presenter.abs;

import com.github.langsky.qingmang.mvp.view.abs.IView;

/**
 * Created by swd1 on 16-12-30.
 */

public interface IPresenter<V extends IView> {

    void bindViewModel(V v);

    void registerRxBus();

    void unregisterRxBus();

}

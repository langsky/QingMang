package com.github.langsky.qingmang.mvp.presenter.abs;


/**
 * Created by swd1 on 17-1-10.
 */

public interface NetPresenter extends IPresenter {

    void registerNetData(String url);

    void refreshData();
}

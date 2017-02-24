package com.github.langsky.qingmang.widget.layout;

import com.github.langsky.qingmang.mvp.model.abs.TextModel;

public interface ArticleViewHandler<T extends TextModel> {

    //设置UI显示格式
    enum UiFlags {
        NO_IMAGE_LOADING
    }

    void setUiData(T t);
}

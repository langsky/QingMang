package com.github.langsky.qingmang.event;

import com.github.langsky.qingmang.mvp.model.Article;
import com.github.langsky.qingmang.mvp.model.Magazine;

/**
 * Created by swd1 on 17-1-18.
 */

public class ArticleEvent {

    private final int POSITION_ADD_DEFAULT = 2;

    private boolean addList;
    private Article article;
    private int position = POSITION_ADD_DEFAULT;

    public ArticleEvent(Article article, boolean addList) {
        this.article = article;
        this.addList = addList;
    }

    public ArticleEvent(Article article, boolean addList, int position) {
        this.article = article;
        this.addList = addList;
        this.position = position;
    }

    public Article getArticle() {
        return article;
    }

    public int getPosition() {
        return position;
    }
}

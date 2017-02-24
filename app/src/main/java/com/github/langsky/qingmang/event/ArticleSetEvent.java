package com.github.langsky.qingmang.event;

/**
 * Created by swd1 on 17-1-24.
 */

public class ArticleSetEvent {

    private final String url;

    public ArticleSetEvent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}

package com.github.langsky.qingmang.event;

/**
 * Created by swd1 on 17-1-25.
 */

public class StringEvent {

    public static final String EVENT_ARTICLE_HISTORY_LIST_EMPTY = "event_article_history_list_empty";
    public static final String EVENT_ARTICLE_HISTORY_LIST_REFRESH = "event_article_history_list_refresh";
    public static final String EVENT_MAGAZINE_HISTORY_LIST_EMPTY = "event_magazine_history_list_empty";
    public static final String EVENT_MAGAZINE_TRANSFORM_HISTORY = "event_magazine_transform_history";
    public static final String EVENT_VIEWPAGER_SCROLL = "event_viewpager_scroll";

    private String message;

    public StringEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}


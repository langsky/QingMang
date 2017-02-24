package com.github.langsky.qingmang.mvp.model.abs;

/**
 * Created by swd1 on 17-1-22.
 */

public abstract class TextModel {

    protected String cover;
    protected String title;
    protected String summary;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

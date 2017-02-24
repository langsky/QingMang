package com.github.langsky.qingmang.mvp.model;

import com.github.langsky.qingmang.mvp.model.abs.TextModel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Locale;

/**
 * Created by swd1 on 17-1-9.
 */
@Entity
public class Magazine extends TextModel{

    @Id
    private Long id;

    private Long setId;

    @Index(unique = true)
    private String title;
    private String summary;
    private String cover;
    private String url;

    @Index(unique = true)
    private Long currentTime;

    private Boolean subscribe;

    @Generated(hash = 362672617)
    public Magazine(Long id, Long setId, String title, String summary, String cover, String url, Long currentTime, Boolean subscribe) {
        this.id = id;
        this.setId = setId;
        this.title = title;
        this.summary = summary;
        this.cover = cover;
        this.url = url;
        this.currentTime = currentTime;
        this.subscribe = subscribe;
    }

    @Generated(hash = 203756486)
    public Magazine() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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


    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "title: %s, summary: %s, cover: %s, url: %s", getTitle(), getSummary(), getCover(), getUrl());
    }

    public Boolean getSubscribe() {
        return this.subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSetId() {
        return this.setId;
    }

    public void setSetId(Long setId) {
        this.setId = setId;
    }

    public Long getCurrentTime() {
        return this.currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }
}

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
public class Article extends TextModel{

    @Id
    private Long id;

    private String cover;

    private String title;
    private String summary;
    private String author;
    private String photo;

    @Index(unique = true)
    private Long currentTime;

    @Index(unique = true)
    private String url;

    @Index(unique = true)
    private String realUrl;

    @Generated(hash = 797088858)
    public Article(Long id, String cover, String title, String summary, String author, String photo, Long currentTime, String url, String realUrl) {
        this.id = id;
        this.cover = cover;
        this.title = title;
        this.summary = summary;
        this.author = author;
        this.photo = photo;
        this.currentTime = currentTime;
        this.url = url;
        this.realUrl = realUrl;
    }

    @Generated(hash = 742516792)
    public Article() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "title: %s, summary: %s, cover: %s, url: %s, author: %s, photo: %s", getTitle(), getSummary(), getCover(), getUrl(), getAuthor(), getPhoto());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCurrentTime() {
        return this.currentTime;
    }

    public void setCurrentTime(Long currentTime) {
        this.currentTime = currentTime;
    }

    public String getRealUrl() {
        return this.realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }

}

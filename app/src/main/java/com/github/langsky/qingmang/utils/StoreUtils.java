package com.github.langsky.qingmang.utils;

import android.content.Context;
import android.util.Log;

import com.github.langsky.qingmang.QingMang;
import com.github.langsky.qingmang.mvp.model.Article;
import com.github.langsky.qingmang.mvp.model.ArticleDao;
import com.github.langsky.qingmang.mvp.model.Magazine;
import com.github.langsky.qingmang.mvp.model.MagazineDao;

import java.util.Collections;
import java.util.List;

/**
 * Created by swd1 on 17-1-16.
 */

public class StoreUtils {

    //history article methods
    private static final String TAG = "StoreUtils";

    /**
     * judge whether an article is historic.
     *
     * @param article article object
     * @return boolean value
     */
    public static boolean isHistoryArticle(Article article) {
        return !QingMang.instance
                .getDaoSession().getArticleDao()
                .queryBuilder().where(ArticleDao.Properties.Url.eq(article.getUrl())).build()
                .list().isEmpty();
    }

    public static boolean isHistoryMagazine(Magazine magazine) {
        return !QingMang.instance
                .getDaoSession().getMagazineDao()
                .queryBuilder()
                .where(MagazineDao.Properties.Url.eq(magazine.getUrl()))
                .build()
                .list().isEmpty();
    }


    /**
     * add ab article to favor list
     *
     * @param article article object
     */
    public static void setArticleHistoric(Article article, String url) {
        if (!isHistoryArticle(article))
            QingMang.instance.getDaoSession().getArticleDao().insertOrReplace(article);
        else {
            Article a = QingMang.instance.getDaoSession()
                    .getArticleDao().queryBuilder()
                    .where(ArticleDao.Properties.Url.eq(article.getUrl())).build()
                    .unique();
            a.setCurrentTime(article.getCurrentTime());
            a.setRealUrl(url);
            QingMang.instance.getDaoSession().getArticleDao().update(a);
        }
    }

    public static void setMagazineHistoric(Magazine magazine) {
        if (!isHistoryMagazine(magazine))
            QingMang.instance.getDaoSession().getMagazineDao().insertOrReplace(magazine);
        else {
            Magazine m = QingMang.instance.getDaoSession()
                    .getMagazineDao().queryBuilder()
                    .where(MagazineDao.Properties.Url.eq(magazine.getUrl())).build()
                    .unique();
            m.setCurrentTime(magazine.getCurrentTime());
            QingMang.instance.getDaoSession().getMagazineDao().update(m);
        }
    }

    /**
     * remove an object from favor list
     *
     * @param article article object
     */
    public static void removeHistoryArticle(Article article) {
        if (!isHistoryArticle(article))
            return;
        Article a = QingMang.instance.getDaoSession().getArticleDao()
                .queryBuilder().where(ArticleDao.Properties.Url.eq(article.getUrl()))
                .build().unique();
        QingMang.instance.getDaoSession().getArticleDao().delete(a);
    }

    public static void removeHistoryMagazine(Magazine magazine) {
        if (!isHistoryMagazine(magazine))
            return;
        Magazine m = QingMang.instance.getDaoSession()
                .getMagazineDao().queryBuilder()
                .where(ArticleDao.Properties.Url.eq(magazine.getUrl())).build()
                .unique();
        QingMang.instance.getDaoSession().getMagazineDao().delete(m);
    }

    public static void removeAllHistoryArticle() {
        QingMang.instance.getDaoSession().getArticleDao().deleteAll();
    }

    public static void removeAllHistoryMagazine() {
        QingMang.instance.getDaoSession().getMagazineDao().deleteAll();
    }

    /**
     * first launch
     *
     * @return boolean value
     */
    public static boolean isFirstLaunch() {
        return getBooleanValue("first_launch", true);
    }

    /**
     * set first launch
     *
     * @param value boolean value
     */
    public static void setFirstLaunch(boolean value) {
        setBooleanValue("first_launch", value);
    }


    /**
     * get all favorite article list
     *
     * @return list
     */
    public static List<Article> getAllHistoryArticles() {
        return QingMang.instance.getDaoSession()
                .getArticleDao().queryBuilder()
                .orderDesc(ArticleDao.Properties.CurrentTime).build().list();
    }

    public static List<Magazine> getAllHistoryMagazines() {
        return QingMang.instance.getDaoSession()
                .getMagazineDao().queryBuilder()
                .orderDesc(MagazineDao.Properties.CurrentTime).build().list();
    }

    private static boolean getBooleanValue(String key, boolean defaultValue) {
        return QingMang.instance.getSharedPreferences("sp", Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    private static void setBooleanValue(String key, boolean value) {
        QingMang.instance.getSharedPreferences("sp", Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }
}

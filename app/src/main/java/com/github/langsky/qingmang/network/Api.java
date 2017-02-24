package com.github.langsky.qingmang.network;


import android.accounts.NetworkErrorException;
import android.util.Log;

import com.github.langsky.qingmang.QingMang;
import com.github.langsky.qingmang.mvp.model.ArticleSet;
import com.github.langsky.qingmang.mvp.model.MagazineSet;
import com.github.langsky.qingmang.utils.DocParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by swd1 on 16-12-29.
 */

public class Api {

    private static final String TAG = "Api";
    private static final MagazineFunc mFunc = new MagazineFunc();
    private static final ArticleFunc aFunc = new ArticleFunc();
    private static final RealUrlFunc uFunc = new RealUrlFunc();


    private static Observable<Document> requestNetwork(final String url) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!NetworkState.isNetworkAvailable(QingMang.instance))
                    subscriber.onError(new NetworkErrorException());
                subscriber.onStart();
                subscriber.onNext(url);
                subscriber.onCompleted();
            }
        }).map(new Func1<String, Document>() {
            @Override
            public Document call(String s) {
                try {
                    Log.e(TAG, "call: " + s);
                    return Jsoup.connect(s).userAgent("Chrome/54.0.2840.100").timeout(5000).get();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
    }

    /**
     * get all magazines from network, if there are no network available, an error will be thrown.
     *
     * @param url network url
     * @return all magazines.
     */
    public static Observable<List<MagazineSet>> getMagazines(final String url) {
        return requestNetwork(url).map(mFunc).subscribeOn(Schedulers.io());
    }

    /**
     * get direct articles from network.
     *
     * @param url network url.
     * @return all articles.
     */
    public static Observable<ArticleSet> getArticles(final String url) {
        return requestNetwork(url).map(aFunc).subscribeOn(Schedulers.io());
    }

    public static Observable<String> getRealUrl(final String url) {
        return requestNetwork(url).map(uFunc).subscribeOn(Schedulers.io());
    }


    private static class MagazineFunc implements Func1<Document, List<MagazineSet>> {
        @Override
        public List<MagazineSet> call(Document document) {
            return DocParser.getMagazineSet(document);
        }
    }

    private static class RealUrlFunc implements Func1<Document, String> {

        @Override
        public String call(Document document) {
            return DocParser.getArticleRealUrl(document);
        }
    }

    private static class ArticleFunc implements Func1<Document, ArticleSet> {
        @Override
        public ArticleSet call(Document document) {
            Log.e(TAG, "解析article文档");
            return DocParser.getArticleSet(document);
        }
    }

}

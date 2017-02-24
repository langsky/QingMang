package com.github.langsky.qingmang.utils;

import android.util.Log;

import com.github.langsky.qingmang.QingMang;
import com.github.langsky.qingmang.mvp.model.Article;
import com.github.langsky.qingmang.mvp.model.ArticleDao;
import com.github.langsky.qingmang.mvp.model.ArticleSet;
import com.github.langsky.qingmang.mvp.model.Magazine;
import com.github.langsky.qingmang.mvp.model.MagazineDao;
import com.github.langsky.qingmang.mvp.model.MagazineSet;
import com.github.langsky.qingmang.mvp.model.MagazineSetDao;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Parse document Created by swd1 on 16-12-30.
 */

public class DocParser {

    private static final String TAG = "DocParser";

    private static final Pattern p1 = Pattern.compile("\\(");
    private static final Pattern p2 = Pattern.compile("\\)");


    /**
     * parse articles in some direct date.
     *
     * @param document document object
     * @return article set object.
     */
    public static ArticleSet getArticleSet(Document document) {
        /**
         * First, get the essential node of document, for example, elements below is the article set what we are interested in,
         * and element below is the node of navigation.
         */
        Elements elements = document.getElementsByClass("articles").first().getElementsByClass("grid row").first().children();
        Element element = document.getElementsByClass("pagination").first();

        /**
         * I need know the count of navigation items, or Parsing will be failed.
         * you will find I just want to get url or title of each navigation item.
         */
        int count = element.children().size();
        String prevUrl = element.select("a").first().attr("abs:href");
        String nextUrl = element.select("a").get(count - 1).attr("abs:href");
        String title = count == 3 ? element.getElementsByClass("active").first().text() : "";

        final List<Article> articles = new ArrayList<>();

        if (elements != null) {
            for (Element e : elements) {
                /**
                 * I will get url, title, cover url, summary, author and author photo of each article.
                 */
                final Article a = new Article();
                a.setUrl(e.select("a").first().attr("abs:href"));
                a.setTitle(e.select("h3").text());
                a.setCover(e.select("a").first().children().size() == 4
                        ? parseUrl(e.getElementsByClass("cover").first().attr("style")) : "");
                a.setSummary(e.getElementsByClass("lead").first().text());
                a.setAuthor(e.getElementsByClass("meta").first().text());
                a.setPhoto(e.getElementsByClass("meta").first().select("img").first().attr("src"));
                articles.add(a);
                Log.i(TAG, "article info " + a.toString());
            }
        }
        final ArticleSet articleSet = new ArticleSet();
        articleSet.setTitle(title);
        articleSet.setPrevUrl(prevUrl);
        articleSet.setNextUrl(nextUrl);
        articleSet.setArticles(articles);
        return articleSet;
    }

    /**
     * Parse all magazines in document
     *
     * @param document document object
     * @return magazine list
     */
    public static List<MagazineSet> getMagazineSet(Document document) {
        final Elements elements = document.select("main").first().children();
        final List<MagazineSet> magazineSets = new ArrayList<>();

        long mId = 0, sId = 0;
        if (elements != null) {
            for (Element element :
                    elements) {
                MagazineSet magazineSet = new MagazineSet();
                magazineSet.setId(sId);
                magazineSet.setTitle(element.select("h2").first().text());
                List<Magazine> magazines = new ArrayList<>();
                for (Element e :
                        element.getElementsByClass("magazine")) {
                    final Magazine m = new Magazine();
                    m.setId(mId);
                    m.setSetId(sId);
                    m.setTitle(e.select("h3").first().text());
                    m.setUrl(e.select("a").first().attr("abs:href"));
                    m.setSummary(e.getElementsByClass("meta").first().text());
                    m.setCover(parseUrl(e.getElementsByClass("cover").first().attr("style")));
                    magazines.add(m);
                    mId++;
                }
                magazineSet.setMagazines(magazines);
                magazineSets.add(magazineSet);
                sId++;
            }
        }
        return magazineSets;
    }

    public static String getArticleRealUrl(Document document) {
        String url = document.getElementsByClass("mask text-center container").first().getElementsByClass("lead").first().select("a").attr("href");
        Log.e(TAG, "getArticleRealUrl: " + url);
        return url;
    }

    private static String parseUrl(String string) {
        String[] s1 = p1.split(string);
        String[] s2 = p2.split(s1[1]);
        return s2[0];
    }

}

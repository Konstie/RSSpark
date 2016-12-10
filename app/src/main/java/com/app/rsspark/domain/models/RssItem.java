package com.app.rsspark.domain.models;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by kmikhailovskiy on 08.12.2016.
 */

public class RssItem extends RealmObject {
    @PrimaryKey private int id;
    private String title;
    private String url;
    private Date savedDate;
    private RealmList<NewsItem> newsItems;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public RealmList<NewsItem> getNewsItems() {
        return newsItems;
    }

    public void setNewsItems(RealmList<NewsItem> newsItems) {
        this.newsItems = newsItems;
    }

    public Date getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(Date savedDate) {
        this.savedDate = savedDate;
    }

    @Override
    public String toString() {
        return "RssItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", savedDate=" + savedDate +
                ", newsItems=" + newsItems +
                '}';
    }
}

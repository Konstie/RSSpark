package com.app.rsspark.domain.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by kmikhailovskiy on 08.12.2016.
 */

public class RssChannel extends RealmObject {
    @PrimaryKey private String title;
    private Date savedDate;
    private RealmList<NewsItem> itemList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RealmList<NewsItem> getItemList() {
        return itemList;
    }

    public void setItemList(RealmList<NewsItem> itemList) {
        this.itemList = itemList;
    }

    public Date getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(Date savedDate) {
        this.savedDate = savedDate;
    }

    @Override
    public String toString() {
        return "RssChannel{" +
                ", title='" + title + '\'' +
                ", savedDate=" + savedDate +
                ", itemList=" + itemList +
                '}';
    }
}

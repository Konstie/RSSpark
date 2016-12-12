package com.app.rsspark.domain.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by konstie on 11.12.16.
 */

@Root(name = "channel", strict = false)
public class Channel implements Serializable {
    @ElementList(entry = "item", required = true, inline = true)
    private RealmList<NewsItem> newsItems;

    public RealmList<NewsItem> getNewsItems() {
        return newsItems;
    }

    public void setNewsItems(RealmList<NewsItem> newsItems) {
        this.newsItems = newsItems;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "newsItems=" + newsItems +
                '}';
    }
}

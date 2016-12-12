package com.app.rsspark.domain.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by kmikhailovskiy on 08.12.2016.
 */

@Root(name = "item", strict = false)
public class NewsItem extends RealmObject {
    @Element(name = "title", required = true)
    @PrimaryKey private String title;
    @Element(name = "link", required = true)
    private String link;
    @Element(name = "description", required = true)
    private String description;
    @Element(name = "pubDate", required = false)
    private String pubDate;
    private String imageUrl;
    private int rssFeedId;

    public NewsItem() {
    }

    public int getRssFeedId() {
        return rssFeedId;
    }

    public void setRssFeedId(int rssFeedId) {
        this.rssFeedId = rssFeedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + pubDate + '\'' +
                '}';
    }
}

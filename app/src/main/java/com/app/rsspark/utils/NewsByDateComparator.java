package com.app.rsspark.utils;

import com.app.rsspark.domain.models.NewsItem;

import java.util.Comparator;

/**
 * Created by kmikhailovskiy on 12.12.2016.
 */

public class NewsByDateComparator implements Comparator<NewsItem> {
    @Override
    public int compare(NewsItem newsItem, NewsItem t1) {
        return newsItem.getRawDate().compareTo(t1.getRawDate());
    }
}

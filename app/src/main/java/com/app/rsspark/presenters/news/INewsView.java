package com.app.rsspark.presenters.news;

import com.app.rsspark.domain.models.NewsItem;

import io.realm.RealmResults;

/**
 * Created by konstie on 11.12.16.
 */

public interface INewsView {
    void onNewsLoaded(RealmResults<NewsItem> newsItems);
    void onInvalidFeed();
}

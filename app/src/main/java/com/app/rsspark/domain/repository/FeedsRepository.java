package com.app.rsspark.domain.repository;

import com.app.rsspark.domain.models.NewsItem;
import com.app.rsspark.domain.models.RssChannel;

import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;

/**
 * @author kmykhaylovskyy
 * Only Feeds-entity specific CRUD- and filtering operations are declared here
 */

public interface FeedsRepository {
    Observable<RssChannel> newRssSource(String title);
    RssChannel findRssSourceByTitle(String title);
    Observable<RealmResults<NewsItem>> saveNewsToCache(RssChannel rssChannel, RealmList<NewsItem> newsItems);
    void removeRssChannelByPrimaryKey(String title);
}
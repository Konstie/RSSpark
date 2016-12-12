package com.app.rsspark.domain.repository;

import com.app.rsspark.domain.contract.RSSParkDatabaseContract;
import com.app.rsspark.domain.models.NewsItem;
import com.app.rsspark.domain.models.RssChannel;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by konstie on 10.12.16.
 */

public class NewsStorage extends BaseStorage<NewsItem> implements NewsRepository {
    public NewsStorage(Realm realm) {
        super(realm);
    }

    @Override
    public Observable<NewsItem> newNewsItem(String title, String description, String link, String imageUrl, Date date) {
        return Observable.create((Observable.OnSubscribe<NewsItem>) subscriber -> {
            realm.executeTransactionAsync(realm1 -> {
                NewsItem newsItem = new NewsItem();
                newsItem.setTitle(title);
                newsItem.setDescription(description);
                realm1.copyToRealm(newsItem);
                subscriber.onNext(newsItem);
                subscriber.onCompleted();
            });
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public RealmResults<NewsItem> getAllNewsForRssFeed(String feedTitle) {
        RssChannel rssChannel = realm.where(RssChannel.class)
                .equalTo(RSSParkDatabaseContract.FIELD_TITLE, feedTitle)
                .findAllSorted(RSSParkDatabaseContract.FIELD_SAVED_DATE).first();
        return rssChannel.getItemList().sort(RSSParkDatabaseContract.FIELD_DATE);
    }
}

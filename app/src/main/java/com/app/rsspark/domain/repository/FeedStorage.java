package com.app.rsspark.domain.repository;

import com.app.rsspark.domain.contract.RSSParkDatabaseContract;
import com.app.rsspark.domain.models.RssItem;

import java.util.Date;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by konstie on 10.12.16.
 */

public class FeedStorage extends BaseStorage<RssItem> implements FeedsRepository {
    public FeedStorage(Realm realm) {
        super(realm);
    }

    @Override
    public Observable<RssItem> newRssSource(String title, String url) {
        return Observable.create(new Observable.OnSubscribe<RssItem>() {
            @Override
            public void call(Subscriber<? super RssItem> subscriber) {
                realm.executeTransactionAsync(realm1 -> {
                    RssItem rssItem = realm1.createObject(RssItem.class);
                    int newId = getMaxItemId(RssItem.class);
                    rssItem.setId(newId);
                    rssItem.setTitle(title);
                    rssItem.setSavedDate(new Date());
                    subscriber.onNext(rssItem);
                    subscriber.onCompleted();
                });
            }
        }).subscribeOn(Schedulers.io());
    }
}

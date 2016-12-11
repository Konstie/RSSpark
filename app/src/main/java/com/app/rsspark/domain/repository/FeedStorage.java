package com.app.rsspark.domain.repository;

import com.app.rsspark.domain.models.RssItem;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;

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
                    int newId = getMaxItemId(RssItem.class);
                    RssItem rssItem = realm1.createObject(RssItem.class, newId);
                    rssItem.setTitle(title);
                    rssItem.setSavedDate(new Date());
                    subscriber.onNext(rssItem);
                    subscriber.onCompleted();
                });
            }
        });
    }
}

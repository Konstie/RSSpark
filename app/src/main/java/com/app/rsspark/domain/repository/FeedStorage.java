package com.app.rsspark.domain.repository;

import com.app.rsspark.domain.contract.RSSParkDatabaseContract;
import com.app.rsspark.domain.models.NewsItem;
import com.app.rsspark.domain.models.RssItem;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

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
                realm.beginTransaction();
                int newId = getMaxItemId(RssItem.class);
                RssItem rssItem = realm.createObject(RssItem.class, newId);
                rssItem.setTitle(title);
                rssItem.setSavedDate(new Date());
                rssItem.setUrl(url);
                realm.commitTransaction();
                subscriber.onNext(rssItem);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<RssItem> findRssSourceById(int id) {
        return realm.where(RssItem.class)
                .equalTo(RSSParkDatabaseContract.FIELD_ID, id)
                .findFirst()
                .asObservable();
    }

    @Override
    public Observable<RealmResults<NewsItem>> findNewsForRssFeedWithId(int id) {
        return findRssSourceById(id)
                .flatMap(new Func1<RssItem, Observable<RealmResults<NewsItem>>>() {
                    @Override
                    public Observable<RealmResults<NewsItem>> call(RssItem rssItem) {
                        RealmList<NewsItem> newsItems = rssItem.getNewsItems();
                        if (newsItems == null || newsItems.isEmpty()) {
                            return Observable.empty();
                        }
                        RealmResults<NewsItem> newsItemsSorted = newsItems
                                .sort(RSSParkDatabaseContract.FIELD_SAVED_DATE, Sort.DESCENDING);
                        return Observable.just(newsItemsSorted);
                    }
                });
    }
}

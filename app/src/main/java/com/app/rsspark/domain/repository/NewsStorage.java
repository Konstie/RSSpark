package com.app.rsspark.domain.repository;

import com.app.rsspark.domain.models.NewsItem;

import java.util.Date;

import io.realm.Realm;
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
                newsItem.setId(getMaxItemId(NewsItem.class));
                newsItem.setTitle(title);
                newsItem.setDescription(description);
                newsItem.setArticleUrl(link);
                newsItem.setImageUrl(imageUrl);
                realm1.copyToRealm(newsItem);
                subscriber.onNext(newsItem);
                subscriber.onCompleted();
            });
        }).subscribeOn(Schedulers.io());
    }
}

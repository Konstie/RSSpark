package com.app.rsspark.domain.repository;

import android.util.Log;

import com.app.rsspark.domain.contract.RSSParkConstants;
import com.app.rsspark.domain.models.NewsItem;
import com.app.rsspark.domain.models.RssChannel;
import com.app.rsspark.network.services.RssRetrievalService;
import com.app.rsspark.utils.FormattingUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by konstie on 10.12.16.
 */

public class FeedStorage extends BaseStorage<RssChannel> implements FeedsRepository {
    private static final String TAG = "FeedStorage";
    private static final String HTML_TAG_IMG = "img";
    private static final String HTML_ATTR_SRC = "src";

    @Inject RssRetrievalService rssService;

    public FeedStorage(Realm realm) {
        super(realm);
    }

    @Override
    public Observable<RssChannel> newRssSource(String title) {
        return Observable.create(new Observable.OnSubscribe<RssChannel>() {
            @Override
            public void call(Subscriber<? super RssChannel> subscriber) {
                realm.beginTransaction();
                RssChannel rssChannel = realm.createObject(RssChannel.class, title);
                rssChannel.setSavedDate(new Date());
                realm.commitTransaction();
                subscriber.onNext(rssChannel);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public RssChannel findRssSourceByTitle(String title) {
        return realm.where(RssChannel.class)
                .equalTo(RSSParkConstants.FIELD_TITLE, title)
                .findFirst();
    }

    @Override
    public Observable<RealmResults<NewsItem>> saveNewsToCache(RssChannel rssChannel, RealmList<NewsItem> newsItems) {
        Log.d(TAG, "saveNewsToCache // newsItems size: " + newsItems.size());

        realm.executeTransaction(realm1 -> {
            Log.d(TAG, "realm trans. started");
            for (NewsItem newsItem : newsItems) {
                if (!newsItemAlreadyExistInList(newsItem.getTitle())) {
                    setAdditionalNewsData(newsItem);
                    realm.copyToRealmOrUpdate(newsItem);
                    rssChannel.getItemList().add(newsItem);
                }
            }
            Log.w(TAG, "Completed realm transaction. News items are added");
        });
        return Observable.just(rssChannel.getItemList().sort(RSSParkConstants.FIELD_DATE));
    }

    private boolean newsItemAlreadyExistInList(String newsTitle) {
        RealmResults<RssChannel> newsItems = realm.where(RssChannel.class).contains("itemList.title", newsTitle).findAll();
        Log.d(TAG, "newsItemAlreadyExistInList for " + newsTitle + " = " + newsItems.size());
        return !newsItems.isEmpty();
    }

    private RealmList<NewsItem> getRealmListOf(List<NewsItem> newsItems) {
        RealmList<NewsItem> news = new RealmList<>();
        for (NewsItem newsItem : newsItems) {
            news.add(newsItem);
        }
        return news;
    }

    private void setAdditionalNewsData(NewsItem newsItem) {
        Document document = Jsoup.parse(newsItem.getDescription());
        Element image = document.select(HTML_TAG_IMG).first();
        if (image != null) {
            String imageUrl = image.absUrl(HTML_ATTR_SRC);
            newsItem.setImageUrl(imageUrl);
        }
        document.select(HTML_TAG_IMG).remove();
        newsItem.setRawDate(FormattingUtils.getConvertedDate(newsItem.getPubDate()));
        newsItem.setDescription(document.body().html());
        Log.d(TAG, "Settings news description: " + newsItem.getImageUrl() + ", " + newsItem.getDescription());
    }
}
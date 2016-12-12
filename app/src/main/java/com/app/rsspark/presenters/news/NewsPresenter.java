package com.app.rsspark.presenters.news;

import android.util.Log;

import com.app.rsspark.RSSparkApplication;
import com.app.rsspark.domain.contract.RSSParkConstants;
import com.app.rsspark.domain.models.Channel;
import com.app.rsspark.domain.models.NewsItem;
import com.app.rsspark.domain.models.RSS;
import com.app.rsspark.domain.models.RssChannel;
import com.app.rsspark.domain.repository.FeedStorage;
import com.app.rsspark.domain.repository.NewsStorage;
import com.app.rsspark.injection.components.AppComponent;
import com.app.rsspark.injection.components.DatabaseComponent;
import com.app.rsspark.network.ApiInterceptor;
import com.app.rsspark.network.services.RssRetrievalService;
import com.app.rsspark.presenters.abs.Presenter;

import javax.inject.Inject;

import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by konstie on 11.12.16.
 */

public class NewsPresenter implements Presenter<INewsView> {
    private static final String TAG = "NewsPresenter";

    private String newsFeedTitle;
    private Sort sortOrder;
    private RssChannel rssChannel;
    private INewsView view;
    private FeedStorage feedStorage;
    private NewsStorage newsStorage;
    private RealmResults<NewsItem> currentNewsList;
    @Inject ApiInterceptor apiInterceptor;
    @Inject RssRetrievalService rssService;

    public NewsPresenter(String title) {
        this.newsFeedTitle = title;
        this.sortOrder = Sort.DESCENDING; // news sort order by default
        DatabaseComponent databaseComponent = RSSparkApplication.getDatabaseComponent();
        AppComponent appComponent = RSSparkApplication.getDaggerAppComponent();
        appComponent.inject(this);
        feedStorage = databaseComponent.feedStorage();
        newsStorage = databaseComponent.newsStorage();
    }

    public void loadNewsFromCache(boolean allowLoadingFromNetwork) {
        Log.w(TAG, "loadNewsFromCache, feed id: " + newsFeedTitle);
        if (rssChannel == null) {
            rssChannel = feedStorage.findRssSourceByTitle(newsFeedTitle);
        }
        Log.d(TAG, "Loaded rss Channel: " + rssChannel);
        RealmList<NewsItem> newsItems = rssChannel.getItemList();
        if (newsItems == null) {
            return;
        }
        currentNewsList = newsItems.sort(RSSParkConstants.FIELD_RAW_DATE, Sort.DESCENDING);
        view.onNewsLoaded(currentNewsList);
        if (newsItems.isEmpty() && allowLoadingFromNetwork) {
            loadNewsFeedFromNetwork();
        }
    }

    public void sortNewsFromCache() {
        Log.w(TAG, "sortNewsFromCache() pressed");
        if (rssChannel == null || !rssChannel.isValid() || rssChannel.getItemList() == null || rssChannel.getItemList().isEmpty()) {
            return;
        }
        sortOrder = sortOrder == Sort.DESCENDING ? Sort.ASCENDING : Sort.DESCENDING;
        currentNewsList = rssChannel.getItemList().sort(RSSParkConstants.FIELD_RAW_DATE, sortOrder);
        view.onNewsSorted(currentNewsList);
    }

    public void loadNewsFeedFromNetwork() {
        Observable<RSS> rssObservable = rssService.getRssFeed(newsFeedTitle);
        rssObservable
                .subscribeOn(Schedulers.newThread())
                .map(RSS::getChannel)
                .map(Channel::getNewsItems)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsItems -> {
                    Log.w(TAG, "Response successful: " + newsItems.toString());
                    onNewsSavedToCache(newsItems);
                }, throwable -> {
                    Log.e(TAG, "Failed: " + throwable.getMessage());
                    view.onInvalidFeed();
                });
    }

    private void onNewsSavedToCache(RealmList<NewsItem> newsItems) {
        Log.i(TAG, "onNewsSavedToCache");
        feedStorage.saveNewsToCache(rssChannel, newsItems)
                .subscribe(this::showNewsItems, throwable -> {
                    Log.e(TAG, "Could not save news to cache: " + throwable.getMessage());
                });
    }

    private void showNewsItems(RealmResults<NewsItem> cachedNews) {
        Log.d(TAG, "showNewsItems {" + cachedNews.size() + "}");
        view.onNewsLoaded(cachedNews);
    }

    @Override
    public void onViewAttached(INewsView view) {
        this.view = view;
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    @Override
    public void onDestroyed() {

    }
}

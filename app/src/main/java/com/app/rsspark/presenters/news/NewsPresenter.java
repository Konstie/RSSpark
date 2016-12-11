package com.app.rsspark.presenters.news;

import android.util.Log;

import com.app.rsspark.domain.repository.FeedStorage;
import com.app.rsspark.domain.repository.NewsStorage;
import com.app.rsspark.injection.components.DaggerDatabaseComponent;
import com.app.rsspark.injection.components.DatabaseComponent;
import com.app.rsspark.presenters.abs.Presenter;

import javax.inject.Inject;

/**
 * Created by konstie on 11.12.16.
 */

public class NewsPresenter implements Presenter<INewsView> {
    private static final String TAG = "NewsPresenter";

    private int newsFeedId;
    private INewsView view;
    @Inject FeedStorage feedStorage;
    @Inject NewsStorage newsStorage;

    public NewsPresenter(int newsFeedId) {
        this.newsFeedId = newsFeedId;
        DatabaseComponent databaseComponent = DaggerDatabaseComponent.builder().build();
        databaseComponent.inject(this);
    }

    public void loadAvailableNews() {
        feedStorage.findNewsForRssFeedWithId(newsFeedId)
                /* .switchIfEmpty() load from web */
                .subscribe(newsItems -> {
                    view.onNewsLoaded(newsItems);
                }, throwable -> {
                    Log.e(TAG, "Could not load news for this feed: " + throwable.getMessage());
                });
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

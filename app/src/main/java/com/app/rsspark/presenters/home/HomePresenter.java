package com.app.rsspark.presenters.home;

import android.util.Log;

import com.app.rsspark.RSSparkApplication;
import com.app.rsspark.domain.models.RssItem;
import com.app.rsspark.domain.repository.FeedStorage;
import com.app.rsspark.presenters.abs.Presenter;

import javax.inject.Inject;

/**
 * Created by konstie on 10.12.16.
 */

public class HomePresenter implements Presenter<IHomeView> {
    private static final String TAG = "HomePresenter";

    private IHomeView view;
    @Inject FeedStorage feedStorage;

    public HomePresenter() {
        feedStorage = RSSparkApplication.getDatabaseComponent().feedStorage();
    }

    @Override
    public void onViewAttached(IHomeView view) {
        this.view = view;
    }

    public void loadStoredFeeds() {
        feedStorage.getAllItems(RssItem.class)
                .subscribe(rssItems -> {
                    view.onRssSourcesInitialized(rssItems);
                }, throwable -> {
                    Log.e(TAG, "Could not load feeds saved by user: " + throwable.getMessage());
                });
    }

    public void saveNewRssFeed(String title, String url) {
        feedStorage.newRssSource(title, url)
                .subscribe(rssItem -> {
                    Log.i(TAG, "RSS item " + rssItem + " was saved successfully!");
                    view.onNewRssSourceAdded(rssItem);
                }, throwable -> {
                    Log.e(TAG, "Could not save RSS-feed: " + throwable.getMessage());
                });
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    /**
     * Performs stored data cleanup
     */
    @Override
    public void onDestroyed() {

    }
}

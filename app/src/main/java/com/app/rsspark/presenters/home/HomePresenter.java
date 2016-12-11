package com.app.rsspark.presenters.home;

import android.util.Log;
import android.util.Pair;

import com.app.rsspark.RSSparkApplication;
import com.app.rsspark.domain.models.RssItem;
import com.app.rsspark.domain.repository.FeedStorage;
import com.app.rsspark.injection.components.DaggerDatabaseComponent;
import com.app.rsspark.injection.components.DatabaseComponent;
import com.app.rsspark.presenters.abs.Presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by konstie on 10.12.16.
 */

public class HomePresenter implements Presenter<IHomeView> {
    private static final String TAG = "HomePresenter";

    private IHomeView view;
    private List<Integer> rssFeedIds;
    private List<String> rssFeedTitles;
    @Inject FeedStorage feedStorage;

    public HomePresenter() {
        DatabaseComponent databaseComponent = DaggerDatabaseComponent.builder().build();
        databaseComponent.inject(this);
        this.rssFeedIds = new ArrayList<>();
        this.rssFeedTitles = new ArrayList<>();
    }

    @Override
    public void onViewAttached(IHomeView view) {
        this.view = view;
    }

    public void loadStoredFeeds() {
        feedStorage.getAllItems(RssItem.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rssItems -> {
                    rssFeedIds.clear();
                    rssFeedIds.addAll(getRssSourcesIds(rssItems));
                    rssFeedTitles.clear();
                    rssFeedTitles.addAll(getRssSourcesTitles(rssItems));
                    view.onRssSourcesInitialized(rssItems, rssFeedIds, rssFeedTitles);
                }, throwable -> {
                    Log.e(TAG, "Could not load feeds saved by user: " + throwable.getMessage());
                });
    }

    public void saveNewRssFeed(String title, String url) {
        feedStorage.newRssSource(title, url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rssItem -> {
                    Log.i(TAG, "RSS item " + rssItem + " was saved successfully!");
                    rssFeedIds.add(rssItem.getId());
                    rssFeedTitles.add(rssItem.getTitle());
                    view.onNewRssSourceAdded(rssItem);
                }, throwable -> {
                    Log.e(TAG, "Could not save RSS-feed: " + throwable.getMessage());
                });
    }

    private List<String> getRssSourcesTitles(RealmResults<RssItem> rssItems) {
        List<String> rssTitles = new ArrayList<>();
        for (RssItem rssItem : rssItems) {
            rssTitles.add(rssItem.getTitle());
        }
        return rssTitles;
    }

    private List<Integer> getRssSourcesIds(RealmResults<RssItem> rssItems) {
        List<Integer> rssIds = new ArrayList<>();
        for (RssItem rssItem : rssItems) {
            rssIds.add(rssItem.getId());
        }
        return rssIds;
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

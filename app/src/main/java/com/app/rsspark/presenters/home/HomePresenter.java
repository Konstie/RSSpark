package com.app.rsspark.presenters.home;

import android.util.Log;

import com.app.rsspark.RSSparkApplication;
import com.app.rsspark.domain.contract.RSSParkConstants;
import com.app.rsspark.domain.models.RssChannel;
import com.app.rsspark.domain.repository.FeedStorage;
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
    private List<String> rssChannelsTitles;
    @Inject FeedStorage feedStorage;

    public HomePresenter() {
        DatabaseComponent databaseComponent = RSSparkApplication.getDatabaseComponent();
        databaseComponent.inject(this);
        this.rssChannelsTitles = new ArrayList<>();
    }

    @Override
    public void onViewAttached(IHomeView view) {
        this.view = view;
    }

    /**
     * Loads RSS-channels from cache. Fills lists with parameters required by channels pager
     * adapter in order to instantiate & invalidate fragments with channels immediately
     * without passing realm objects as parameters
     */
    public void loadStoredChannels() {
        Log.w(TAG, "loadStoredChannels called");
        feedStorage.getAllItems(RssChannel.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rssItems -> {
                    Log.w(TAG, "Loaded RSS-channels successfully: " + rssItems);
                    rssChannelsTitles.clear();
                    rssChannelsTitles.addAll(getRssChannelsTitles(rssItems));
                    List<RssChannel> rssChannels = new ArrayList<>(rssItems.where().equalTo(RSSParkConstants.FIELD_WILL_BE_REMOVED, false).findAll());
                    view.onRssSourcesInitialized(rssChannels, rssChannelsTitles);
                }, throwable -> {
                    Log.e(TAG, "Could not load feeds saved by user: " + throwable.getMessage());
                });
    }

    public void saveNewRssFeed(String title) {
        feedStorage.newRssSource(title)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rssItem -> {
                    Log.i(TAG, "RSS item " + rssItem + " was saved successfully!");
                    rssChannelsTitles.add(rssItem.getTitle());
                    view.onNewRssSourceAdded(rssItem);
                }, throwable -> {
                    Log.e(TAG, "Could not save RSS-feed: " + throwable.getMessage());
                });
    }

    private List<String> getRssChannelsTitles(RealmResults<RssChannel> rssChannels) {
        List<String> rssTitles = new ArrayList<>();
        for (RssChannel rssChannel : rssChannels) {
            rssTitles.add(rssChannel.getTitle());
        }
        return rssTitles;
    }

    public void onRssChannelRemoved(String rssChannelTitle, int rssChannelPosition) {
        rssChannelsTitles.remove(rssChannelPosition);
        feedStorage.removeRssChannelByPrimaryKey(rssChannelTitle);
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

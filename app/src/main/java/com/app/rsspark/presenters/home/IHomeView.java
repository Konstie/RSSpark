package com.app.rsspark.presenters.home;

import com.app.rsspark.domain.models.RssItem;

import io.realm.RealmResults;

/**
 * Created by konstie on 10.12.16.
 */

public interface IHomeView {
    void onRssSourcesInitialized(RealmResults<RssItem> rssItems);
    void onNewRssSourceAdded(RssItem rssItem);
}

package com.app.rsspark.presenters.home;

import com.app.rsspark.domain.models.RssItem;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by konstie on 10.12.16.
 */

public interface IHomeView {
    void onRssSourcesInitialized(RealmResults<RssItem> rssItems, List<Integer> rssIds, List<String> rssTitles);
    void onNewRssSourceAdded(RssItem rssItem);
}
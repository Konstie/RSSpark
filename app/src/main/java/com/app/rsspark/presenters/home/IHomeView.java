package com.app.rsspark.presenters.home;

import android.util.Pair;

import com.app.rsspark.domain.models.RssChannel;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by konstie on 10.12.16.
 */

public interface IHomeView {
    void onRssSourcesInitialized(RealmResults<RssChannel> rssChannels, List<String> rssItemsDetails);
    void onNewRssSourceAdded(RssChannel rssChannel);
}
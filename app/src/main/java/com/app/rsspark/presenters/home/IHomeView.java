package com.app.rsspark.presenters.home;

import com.app.rsspark.domain.models.RssChannel;

import java.util.List;

/**
 * Created by konstie on 10.12.16.
 */

public interface IHomeView {
    void onRssSourcesInitialized(List<RssChannel> rssChannels, List<String> rssItemsDetails);
    void onNewRssSourceAdded(RssChannel rssChannel);
}
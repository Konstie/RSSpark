package com.app.rsspark.injection.components;

import com.app.rsspark.domain.repository.FeedStorage;
import com.app.rsspark.domain.repository.NewsStorage;
import com.app.rsspark.injection.modules.DatabaseModule;
import com.app.rsspark.presenters.home.HomePresenter;
import com.app.rsspark.ui.sections.home.RssChannelsAdapter;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;

/**
 * Created by konstie on 10.12.16.
 */

@Singleton
@Component(modules = {DatabaseModule.class})
public interface DatabaseComponent {
    Realm realm();
    FeedStorage feedStorage();
    NewsStorage newsStorage();
    void inject(HomePresenter presenter);
    void inject(RssChannelsAdapter adapter);
}

package com.app.rsspark.injection.modules;

import com.app.rsspark.domain.repository.FeedStorage;
import com.app.rsspark.domain.repository.NewsStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by konstie on 10.12.16.
 */

@Module
public class DatabaseModule {
    @Provides
    @Singleton
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    FeedStorage provideFeedStorage(Realm realm) {
        return new FeedStorage(realm);
    }

    @Provides
    @Singleton
    NewsStorage provideNewsStorage(Realm realm) {
        return new NewsStorage(realm);
    }
}

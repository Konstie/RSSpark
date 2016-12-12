package com.app.rsspark.injection.components;

import android.content.Context;
import android.content.res.Resources;

import com.app.rsspark.domain.repository.FeedStorage;
import com.app.rsspark.injection.modules.AppModule;
import com.app.rsspark.injection.modules.NetworkModule;
import com.app.rsspark.injection.scopes.ForApplication;
import com.app.rsspark.presenters.news.NewsPresenter;
import com.app.rsspark.utils.PreferencesHelper;

import dagger.Component;

/**
 * Created by konstie on 10.12.16.
 */
@ForApplication
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    Context appContext();
    Resources resources();
    PreferencesHelper preferencesHelper();
    void inject(NewsPresenter newsPresenter);
    void inject(FeedStorage feedStorage);
}
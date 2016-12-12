package com.app.rsspark.injection.modules;

import android.content.Context;
import android.content.res.Resources;

import com.app.rsspark.RSSparkApplication;
import com.app.rsspark.injection.scopes.ForApplication;
import com.app.rsspark.utils.PreferencesHelper;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

/**
 * Created by konstie on 10.12.16.
 */

@Module
public class AppModule {
    private RSSparkApplication application;

    public AppModule(RSSparkApplication application) {
        this.application = application;
    }

    @Inject
    @Provides
    @ForApplication
    Context provideAppContext() {
        return application;
    }

    @Provides
    @ForApplication
    Resources provideResources() {
        return application.getResources();
    }

    @Provides
    @ForApplication
    PreferencesHelper providePreferencesHelper() {
        return new PreferencesHelper();
    }
}

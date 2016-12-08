package com.app.rsspark.dagger.modules;

import android.content.Context;

import com.app.RSSparkApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kmikhailovskiy on 08.12.2016.
 */

@Module
public class AppModule {
    private RSSparkApplication application;

    public AppModule(RSSparkApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public RSSparkApplication provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }


}

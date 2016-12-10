package com.app.rsspark.injection.modules;

import android.content.Context;
import android.content.res.Resources;

import com.app.RSSparkApplication;
import com.app.rsspark.injection.scopes.ForApplication;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

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
}

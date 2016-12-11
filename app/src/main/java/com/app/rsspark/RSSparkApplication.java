package com.app.rsspark;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.app.rsspark.injection.components.AppComponent;
import com.app.rsspark.injection.components.DaggerAppComponent;
import com.app.rsspark.injection.components.DaggerDatabaseComponent;
import com.app.rsspark.injection.components.DatabaseComponent;
import com.app.rsspark.injection.modules.AppModule;

import io.realm.Realm;

/**
 * Created by kmikhailovskiy on 08.12.2016.
 */

public class RSSparkApplication extends Application {
    private static AppComponent appComponent = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static Context getContext() {
        return appComponent.appContext();
    }

    public static Resources getAppResources() {
        return appComponent.resources();
    }
}

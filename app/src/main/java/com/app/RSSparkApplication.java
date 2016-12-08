package com.app;

import android.app.Application;

import com.app.rsspark.dagger.components.AppComponent;
import com.app.rsspark.dagger.components.DaggerAppComponent;
import com.app.rsspark.dagger.modules.AppModule;

/**
 * Created by kmikhailovskiy on 08.12.2016.
 */

public class RSSparkApplication extends Application {
    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = new DaggerAppComponent.Builder()
                .appModule(new AppModule(this))
                .build();
    }
}

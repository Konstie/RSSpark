package com.app.rsspark.dagger.components;

import com.app.RSSparkApplication;
import com.app.rsspark.dagger.modules.AppModule;

import dagger.Component;

/**
 * Created by kmikhailovskiy on 08.12.2016.
 */

@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(RSSparkApplication application);
}

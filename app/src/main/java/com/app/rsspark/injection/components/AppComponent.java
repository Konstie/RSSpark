package com.app.rsspark.injection.components;

import android.content.Context;
import android.content.res.Resources;

import com.app.rsspark.injection.modules.AppModule;
import com.app.rsspark.injection.modules.DatabaseModule;
import com.app.rsspark.injection.modules.NetworkModule;
import com.app.rsspark.injection.scopes.ForApplication;

import dagger.Component;
import io.realm.Realm;
import okhttp3.OkHttpClient;

/**
 * Created by konstie on 10.12.16.
 */
@ForApplication
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    Context appContext();
    Resources resources();
    OkHttpClient okHttpClient();
}
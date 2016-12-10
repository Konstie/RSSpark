package com.app.rsspark.injection.modules;

import com.app.rsspark.injection.scopes.ForApplication;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by konstie on 10.12.16.
 */

@Module
public class NetworkModule {
    private static final long CONNECTION_TIMEOUT = 30;

    @Provides
    @ForApplication
    OkHttpClient provideOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }
}

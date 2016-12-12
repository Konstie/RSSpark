package com.app.rsspark.injection.modules;

import com.app.rsspark.injection.scopes.ForApplication;
import com.app.rsspark.network.ApiInterceptor;
import com.app.rsspark.network.services.RssRetrievalService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by konstie on 10.12.16.
 */

@Module
public class NetworkModule {
    private static final long CONNECTION_TIMEOUT = 30;
    private static final String BASE_URL = "http://feeds.feedburner.com/";

    @Provides
    @ForApplication
    ApiInterceptor provideInterceptor() {
        return ApiInterceptor.get();
    }

    @Provides
    @ForApplication
    OkHttpClient provideOkHttpClient(ApiInterceptor interceptor) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        return okHttpClient;
    }

    @Provides
    @ForApplication
    Retrofit.Builder provideRetrofitBuilder(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create());
    }

    @Provides
    @ForApplication
    RssRetrievalService provideRssService(Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.build().create(RssRetrievalService.class);
    }
}
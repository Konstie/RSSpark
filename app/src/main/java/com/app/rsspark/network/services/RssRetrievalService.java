package com.app.rsspark.network.services;

import com.app.rsspark.domain.models.RSS;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by konstie on 11.12.16.
 */

public interface RssRetrievalService {
    @GET
    Observable<RSS> getRssFeed(@Url String feedUrl);
}

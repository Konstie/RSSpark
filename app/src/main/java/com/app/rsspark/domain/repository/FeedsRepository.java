package com.app.rsspark.domain.repository;

import com.app.rsspark.domain.models.RssItem;

import io.realm.RealmResults;
import rx.Observable;

/**
 * @author kmykhaylovskyy
 * Only Feeds-entity specific CRUD- and filtering operations are declared here
 */

public interface FeedsRepository {
    Observable<RssItem> newRssSource(String title, String url);
}
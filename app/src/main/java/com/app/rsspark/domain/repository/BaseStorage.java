package com.app.rsspark.domain.repository;

import com.app.rsspark.domain.contract.RSSParkConstants;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;

/**
 * @author kmykhaylovskyy
 * Generic for all realm entities CRUD-operations are implemented here.
 */

public abstract class BaseStorage<T extends RealmObject> {
    protected Realm realm;

    public BaseStorage(Realm realm) {
        this.realm = realm;
    }

    public Observable<RealmResults<T>> getAllItems(Class<T> instanceClass) {
        return realm.where(instanceClass).findAll().asObservable();
    }

    public Observable<RealmResults<T>> getAllItemsSortedByDate(Class<T> instanceClass, Sort order) {
        return realm.where(instanceClass)
                .isNotNull(RSSParkConstants.FIELD_SAVED_DATE)
                .findAllSorted(RSSParkConstants.FIELD_SAVED_DATE, order)
                .asObservable();
    }

    public void removeItem(T item) {
        realm.executeTransaction(realm -> item.deleteFromRealm());
    }

    public int getMaxItemId(Class<? extends RealmModel> instanceClass) {
        RealmResults<? extends RealmModel> realmResults = Realm.getDefaultInstance().where(instanceClass).findAll();
        if (realmResults.isEmpty()) {
            return 1;
        }
        return realmResults.max(RSSParkConstants.FIELD_ID).intValue() + 1;
    }
}

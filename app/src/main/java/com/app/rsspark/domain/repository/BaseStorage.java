package com.app.rsspark.domain.repository;

import com.app.rsspark.domain.contract.RSSParkDatabaseContract;

import io.realm.Realm;
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
        return realm.where(instanceClass).findAllAsync().asObservable();
    }

    public Observable<RealmResults<T>> getAllItemsSortedByDate(Class<T> instanceClass, Sort order) {
        return realm.where(instanceClass)
                .isNotNull(RSSParkDatabaseContract.FIELD_SAVED_DATE)
                .findAllSorted(RSSParkDatabaseContract.FIELD_SAVED_DATE, order)
                .asObservable();
    }

    public void removeItem(T item) {
        realm.executeTransaction(realm1 -> item.deleteFromRealm());
    }

    public int getMaxItemId(Class<T> instanceClass) {
        return realm.where(instanceClass).max("id").intValue() + 1;
    }
}

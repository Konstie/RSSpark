package com.app.rsspark.presenters.abs;

/**
 * Created by konstie on 08.12.16.
 */

public interface Presenter<V> {
    void onViewAttached(V view);
    void onViewDetached();
    void onDestroyed();
}

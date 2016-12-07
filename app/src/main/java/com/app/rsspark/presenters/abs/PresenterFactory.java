package com.app.rsspark.presenters.abs;

/**
 * Created by konstie on 08.12.16.
 */

public interface PresenterFactory<T extends Presenter> {
    T createPresenter();
}

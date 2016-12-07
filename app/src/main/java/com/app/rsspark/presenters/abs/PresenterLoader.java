package com.app.rsspark.presenters.abs;

import android.content.Context;
import android.support.v4.content.Loader;

/**
 * Created by konstie on 08.12.16.
 */

public class PresenterLoader<T extends Presenter> extends Loader<T> {
    private final PresenterFactory<T> factory;
    private T presenter;

    public PresenterLoader(Context context, PresenterFactory<T> factory) {
        super(context);
        this.factory = factory;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        presenter = factory.createPresenter();

        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        super.onReset();
        if (presenter != null) {
            presenter.onDestroyed();
            presenter = null;
        }
    }
}

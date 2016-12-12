package com.app.rsspark.ui.abs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.app.rsspark.presenters.abs.Presenter;
import com.app.rsspark.presenters.abs.PresenterFactory;
import com.app.rsspark.presenters.abs.PresenterLoader;

/**
 * Created by konstie on 11.12.16.
 */

public abstract class BaseFragment<P extends Presenter<V>, V> extends Fragment {
    private static final String TAG = "NewsListFragment";
    private static final int LOADER_ID = 101;

    private Presenter<V> presenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
    }

    private LoaderManager.LoaderCallbacks<P> loaderCallbacks = new LoaderManager.LoaderCallbacks<P>() {
        @Override
        public Loader<P> onCreateLoader(int id, Bundle args) {
            return new PresenterLoader<>(getContext(), getPresenterFactory());
        }

        @Override
        public void onLoadFinished(Loader<P> loader, P presenter) {
            BaseFragment.this.presenter = presenter;
            onPresenterPrepared(presenter);
        }

        @Override
        public void onLoaderReset(Loader<P> loader) {
            BaseFragment.this.presenter = null;
        }
    };

    @Override
    public void onDestroy() {
        presenter.onViewDetached();
        super.onDestroy();
    }

    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();

    protected abstract void onPresenterPrepared(@NonNull P presenter);

    protected void onPresenterDestroyed() {}
}

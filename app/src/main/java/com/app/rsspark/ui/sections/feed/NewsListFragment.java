package com.app.rsspark.ui.sections.feed;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.rsspark.R;
import com.app.rsspark.domain.contract.RSSParkDatabaseContract;
import com.app.rsspark.domain.models.NewsItem;
import com.app.rsspark.presenters.abs.PresenterFactory;
import com.app.rsspark.presenters.news.INewsView;
import com.app.rsspark.presenters.news.NewsPresenter;
import com.app.rsspark.ui.abs.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by konstie on 11.12.16.
 */

public class NewsListFragment extends BaseFragment<NewsPresenter, INewsView> implements INewsView {
    private static final String TAG = "NewsListFragment";
    private static final String EXTRA_RSS_FEED_ID = "EXTRA_RSS_FEED_ID";

    private int newsFeedId;
    private NewsPresenter newsPresenter;
    private NewsAdapter newsAdapter;

    @BindView(R.id.news_list_view) RecyclerView newsListView;

    public static NewsListFragment newInstance(int rssFeedId) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_RSS_FEED_ID, rssFeedId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            newsFeedId = args.getInt(EXTRA_RSS_FEED_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        ButterKnife.bind(this, rootView);
        Log.d(TAG, "News feed id: " + newsFeedId);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        newsListView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onPresenterPrepared(@NonNull NewsPresenter presenter) {
        this.newsPresenter = presenter;
        this.newsPresenter.onViewAttached(NewsListFragment.this);
        this.newsPresenter.loadAvailableNews();
    }

    @Override
    public void onNewsLoaded(RealmResults<NewsItem> newsItems) {
        if (newsAdapter == null) {
            newsAdapter = new NewsAdapter(getContext(), newsItems, true);
            newsListView.setAdapter(newsAdapter);
        } else {
            newsAdapter.setNewsItems(newsItems);
            newsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_RSS_FEED_ID, newsFeedId);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            newsFeedId = savedInstanceState.getInt(EXTRA_RSS_FEED_ID);
        }
    }

    @NonNull
    public PresenterFactory<NewsPresenter> getPresenterFactory() {
        return () -> new NewsPresenter(newsFeedId);
    }
}

package com.app.rsspark.ui.sections.feed;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.app.rsspark.R;
import com.app.rsspark.domain.models.NewsItem;
import com.app.rsspark.presenters.abs.PresenterFactory;
import com.app.rsspark.presenters.news.INewsView;
import com.app.rsspark.presenters.news.NewsPresenter;
import com.app.rsspark.ui.abs.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by konstie on 11.12.16.
 */

public class NewsListFragment extends BaseFragment<NewsPresenter, INewsView> implements INewsView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NewsListFragment";
    private static final String EXTRA_RSS_FEED_ID = "EXTRA_RSS_FEED_ID";

    private String newsFeedTitle;
    private NewsPresenter newsPresenter;
    private NewsAdapter newsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.layout_swipe_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.news_list_view) RecyclerView newsListView;

    public static NewsListFragment newInstance(String rssFeedId) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_RSS_FEED_ID, rssFeedId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null) {
            newsFeedTitle = args.getString(EXTRA_RSS_FEED_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        ButterKnife.bind(this, rootView);
        Log.d(TAG, "News feed id: " + newsFeedTitle);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutManager = new LinearLayoutManager(getContext());
        newsListView.setLayoutManager(layoutManager);
        newsListView.setHasFixedSize(true);

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        if (newsPresenter != null) {
            newsPresenter.loadNewsFeedFromNetwork();
        }
    }

    @Override
    protected void onPresenterPrepared(@NonNull NewsPresenter presenter) {
        this.newsPresenter = presenter;
        Log.w(TAG, "onPresenterPrepared!");
        this.newsPresenter.onViewAttached(NewsListFragment.this);
        this.newsPresenter.loadNewsFromCache(true);
    }

    @Override
    public void onNewsLoaded(RealmResults<NewsItem> newsItems) {
        Log.w(TAG, "onNewsLoaded count: " + newsItems.size());
        if (newsAdapter == null) {
            newsAdapter = new NewsAdapter(getContext(), newsItems, true);
            newsListView.setAdapter(newsAdapter);
            newsAdapter.notifyDataSetChanged();
        } else {
            newsAdapter.notifyDataSetChanged();
        }
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        Log.w(TAG, "Adapter items count: " + newsAdapter.getItemCount());
    }

    @Override
    public void onNewsSorted(RealmResults<NewsItem> results) {
        if (newsAdapter != null) {
            newsAdapter.setNewsItems(results);
            newsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onInvalidFeed() {
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                if (newsPresenter != null) {
                    newsPresenter.sortNewsFromCache();
                }
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_RSS_FEED_ID, newsFeedTitle);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            newsFeedTitle = savedInstanceState.getString(EXTRA_RSS_FEED_ID);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "Destroyed");
    }

    @NonNull
    public PresenterFactory<NewsPresenter> getPresenterFactory() {
        return () -> new NewsPresenter(newsFeedTitle);
    }
}

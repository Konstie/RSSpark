package com.app.rsspark.ui.sections.home;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.rsspark.R;
import com.app.rsspark.domain.models.RssChannel;
import com.app.rsspark.presenters.abs.PresenterFactory;
import com.app.rsspark.presenters.abs.PresenterLoader;
import com.app.rsspark.presenters.home.HomePresenter;
import com.app.rsspark.presenters.home.IHomeView;
import com.app.rsspark.ui.sections.feed.NewsListFragment;
import com.app.rsspark.ui.sections.feed.RSSPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity implements IHomeView, View.OnClickListener,
        RssChannelCreationDialog.RssDialogListener, LoaderManager.LoaderCallbacks<HomePresenter>,
        FeedItemsAdapter.RssChannelRemoveListener {
    private static final int LOADER_ID = 101;
    private static final String RSS_DIALOG_TAG = "ADD_RSS";
    private static final String TAG = "HomeActivity";

    private PresenterFactory<HomePresenter> presenterFactory = HomePresenter::new;
    private HomePresenter presenter;
    private FeedItemsAdapter adapter;
    private RSSPagerAdapter pagerAdapter;

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.rss_sources_list_view) RecyclerView rssMenuListView;
    @BindView(R.id.btn_add_feed) RelativeLayout buttonAddFeed;
    @BindView(R.id.feeds_tab_layout) TabLayout tabLayout;
    @BindView(R.id.feeds_view_pager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Log.d("HomeActivity", "rssMenulist view: " + rssMenuListView);
        rssMenuListView.setLayoutManager(layoutManager);

        buttonAddFeed.setOnClickListener(this);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRssSourcesInitialized(RealmResults<RssChannel> rssChannels, List<String> rssDetails) {
        Log.d(TAG, "onRssSourcesInitialized: " + rssChannels.size());
        adapter = new FeedItemsAdapter(HomeActivity.this, rssChannels, true, this);
        runOnUiThread(() -> {
            rssMenuListView.setAdapter(adapter);
            invalidateRssFeedsPager(rssDetails);
        });
    }

    private void invalidateRssFeedsPager(List<String> rssDetails) {
        Log.d(TAG, "invalidateRssFeedsPager. Pager adapter: " + pagerAdapter);
        tabLayout.setTabGravity(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setVisibility(rssDetails.isEmpty() ? View.GONE : View.VISIBLE);
        if (pagerAdapter == null) {
            pagerAdapter = new RSSPagerAdapter(getSupportFragmentManager(), getNewsFragments(rssDetails), rssDetails);
            viewPager.setAdapter(pagerAdapter);
        } else {
            pagerAdapter.notifyDataSetChanged();
        }
        viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
    }

    private List<NewsListFragment> getNewsFragments(List<String> rssChannelIds) {
        List<NewsListFragment> fragments = new ArrayList<>();
        for (String rssFeedDetails : rssChannelIds) {
            fragments.add(NewsListFragment.newInstance(rssFeedDetails));
        }
        return fragments;
    }

    @Override
    public void onSaveNewRssFeedClicked(String rssTitle) {
        presenter.saveNewRssFeed(rssTitle);
    }

    @Override
    public void onNewRssSourceAdded(RssChannel rssChannel) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (pagerAdapter != null) {
            pagerAdapter.addNewFragment(rssChannel.getTitle());
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onChannelRemoved(RssChannel rssChannel, int position) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        pagerAdapter.removeFragment(position);
        pagerAdapter.notifyDataSetChanged();
        if (pagerAdapter.getCount() > 0) {
            viewPager.setCurrentItem(0);
        }
    }

    @Override
    public Loader<HomePresenter> onCreateLoader(int id, Bundle args) {
        Log.w(TAG, "LoaderCallbacks -> onCreateLoader");
        return new PresenterLoader<>(HomeActivity.this, presenterFactory);
    }

    @Override
    public void onLoadFinished(Loader<HomePresenter> loader, HomePresenter presenter) {
        Log.w(TAG, "LoaderCallbacks -> onLoadFinished");
        this.presenter = presenter;
        this.presenter.onViewAttached(HomeActivity.this);
        this.presenter.loadStoredChannels();
    }

    @Override
    public void onLoaderReset(Loader<HomePresenter> loader) {
        Log.w(TAG, "LoaderCallbacks -> onLoaderReset");
        this.presenter.onViewDetached();
        this.presenter.onDestroyed();
        this.presenter = null;
    }

    @Override
    public void onClick(View view) {
        if (view == buttonAddFeed) {
            showNewFeedDialog();
        }
    }

    private void showNewFeedDialog() {
        RssChannelCreationDialog dialog = new RssChannelCreationDialog();
        dialog.show(getSupportFragmentManager(), RSS_DIALOG_TAG);
    }

    @Override
    public void showInvalidNewRssMessage(@StringRes int messageRes) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show();
    }
}
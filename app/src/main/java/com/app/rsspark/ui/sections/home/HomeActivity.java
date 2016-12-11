package com.app.rsspark.ui.sections.home;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.app.rsspark.domain.models.RssItem;
import com.app.rsspark.presenters.abs.PresenterFactory;
import com.app.rsspark.presenters.abs.PresenterLoader;
import com.app.rsspark.presenters.home.HomePresenter;
import com.app.rsspark.presenters.home.IHomeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity implements IHomeView, View.OnClickListener,
        RssSourceCreationDialog.RssDialogListener, LoaderManager.LoaderCallbacks<HomePresenter> {
    private static final int LOADER_ID = 101;
    private static final String RSS_DIALOG_TAG = "ADD_RSS";

    private PresenterFactory<HomePresenter> presenterFactory = HomePresenter::new;
    private HomePresenter presenter;
    private FeedItemsAdapter adapter;

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
    public void onRssSourcesInitialized(RealmResults<RssItem> rssItems) {
        adapter = new FeedItemsAdapter(HomeActivity.this, rssItems, true);
        rssMenuListView.setAdapter(adapter);
        // todo: initialize pager adapter
    }

    @Override
    public void onSaveNewRssFeedClicked(String rssTitle, String rssUrl) {
        presenter.saveNewRssFeed(rssTitle, rssUrl);
    }

    @Override
    public void onNewRssSourceAdded(RssItem rssItem) {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public Loader<HomePresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(HomeActivity.this, presenterFactory);
    }

    @Override
    public void onLoadFinished(Loader<HomePresenter> loader, HomePresenter presenter) {
        this.presenter = presenter;
        this.presenter.onViewAttached(HomeActivity.this);
        this.presenter.loadStoredFeeds();
    }

    @Override
    public void onLoaderReset(Loader<HomePresenter> loader) {
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
        RssSourceCreationDialog dialog = new RssSourceCreationDialog();
        dialog.show(getSupportFragmentManager(), RSS_DIALOG_TAG);
    }

    @Override
    public void showInvalidNewRssMessage(@StringRes int messageRes) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show();
    }
}
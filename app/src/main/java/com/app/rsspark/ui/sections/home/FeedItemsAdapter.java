package com.app.rsspark.ui.sections.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rsspark.R;
import com.app.rsspark.domain.models.RssItem;
import com.app.rsspark.domain.repository.FeedStorage;
import com.app.rsspark.injection.components.DaggerDatabaseComponent;
import com.app.rsspark.injection.components.DatabaseComponent;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by kmikhailovskiy on 08.12.2016.
 */

public class FeedItemsAdapter extends RealmRecyclerViewAdapter<RssItem, FeedItemsAdapter.FeedItemHolder> {
    private RealmResults<RssItem> rssSources;
    private DatabaseComponent databaseComponent;
    private FeedStorage feedStorage;

    public FeedItemsAdapter(@NonNull Context context, @Nullable RealmResults<RssItem> rssSources, boolean autoUpdate) {
        super(context, rssSources, autoUpdate);
        this.rssSources = rssSources;
        this.databaseComponent = DaggerDatabaseComponent.builder().build();
        this.feedStorage = databaseComponent.feedStorage();
    }

    @Override
    public FeedItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_rss_feed, parent, false);
        return new FeedItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedItemHolder holder, int position) {
        RssItem rssSource = rssSources.get(position);
        holder.titleTextView.setText(rssSource.getTitle());
        holder.buttonRemoveFeed.setOnClickListener(btn -> {
            onRemoveClicked(rssSource);
        });
    }

    private void onRemoveClicked(RssItem rssItem) {
        feedStorage.removeItem(rssItem);
    }

    static class FeedItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btn_remove_feed) ImageView buttonRemoveFeed;
        @BindView(R.id.feed_title_text_view) TextView titleTextView;

        FeedItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

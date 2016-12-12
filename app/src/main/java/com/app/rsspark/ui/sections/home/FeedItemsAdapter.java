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
import com.app.rsspark.domain.models.RssChannel;
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

public class FeedItemsAdapter extends RealmRecyclerViewAdapter<RssChannel, FeedItemsAdapter.FeedItemHolder> {
    private RealmResults<RssChannel> rssSources;
    private DatabaseComponent databaseComponent;
    private FeedStorage feedStorage;
    private RssChannelRemoveListener listener;

    public FeedItemsAdapter(@NonNull Context context, @Nullable RealmResults<RssChannel> rssSources, boolean autoUpdate, RssChannelRemoveListener listener) {
        super(context, rssSources, autoUpdate);
        this.rssSources = rssSources;
        this.databaseComponent = DaggerDatabaseComponent.builder().build();
        this.feedStorage = databaseComponent.feedStorage();
        this.listener = listener;
    }

    @Override
    public FeedItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_rss_feed, parent, false);
        return new FeedItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedItemHolder holder, int position) {
        RssChannel rssSource = rssSources.get(position);
        holder.titleTextView.setText(rssSource.getTitle());
        holder.buttonRemoveFeed.setOnClickListener(btn -> {
            onRemoveClicked(rssSource, position);
        });
    }

    private void onRemoveClicked(RssChannel rssChannel, int position) {
        feedStorage.removeItem(rssChannel);
        if (listener != null) {
            listener.onChannelRemoved(rssChannel, position);
        }
    }

    static class FeedItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.btn_remove_feed) ImageView buttonRemoveFeed;
        @BindView(R.id.feed_title_text_view) TextView titleTextView;

        FeedItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface RssChannelRemoveListener {
        void onChannelRemoved(RssChannel rssChannel, int position);
    }
}

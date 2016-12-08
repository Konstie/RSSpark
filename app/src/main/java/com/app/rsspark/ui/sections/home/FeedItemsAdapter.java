package com.app.rsspark.ui.sections.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.rsspark.R;
import com.app.rsspark.repository.models.RssItem;

import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by kmikhailovskiy on 08.12.2016.
 */

public class FeedItemsAdapter extends RealmRecyclerViewAdapter<RssItem, FeedItemsAdapter.FeedItemHolder> {
    public FeedItemsAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<RssItem> data, boolean autoUpdate) {
        super(context, data, autoUpdate);
    }

    @Override
    public FeedItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_rss_feed, parent, false);
        return new FeedItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedItemHolder holder, int position) {

    }

    static class FeedItemHolder extends RecyclerView.ViewHolder {
        public FeedItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }
}

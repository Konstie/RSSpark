package com.app.rsspark.ui.sections.home;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rsspark.R;
import com.app.rsspark.RSSparkApplication;
import com.app.rsspark.domain.models.RssChannel;
import com.app.rsspark.domain.repository.FeedStorage;
import com.app.rsspark.injection.components.AppComponent;
import com.app.rsspark.injection.components.DatabaseComponent;
import com.app.rsspark.utils.PreferencesHelper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kmikhailovskiy on 08.12.2016.
 */

public class RssChannelsAdapter extends RecyclerView.Adapter<RssChannelsAdapter.FeedItemHolder> {
    private Context context;
    private List<RssChannel> rssSources;
    private DatabaseComponent databaseComponent;
    @Inject FeedStorage feedStorage;
    private PreferencesHelper preferencesHelper;
    private RssChannelRemoveListener listener;

    public RssChannelsAdapter(@NonNull Context context, @Nullable List<RssChannel> rssSources, RssChannelRemoveListener listener) {
        super();
        this.context = context;
        this.rssSources = rssSources;
        this.listener = listener;
        this.databaseComponent = RSSparkApplication.getDatabaseComponent();
        this.databaseComponent.inject(this);
        AppComponent appComponent = RSSparkApplication.getDaggerAppComponent();
        preferencesHelper = appComponent.preferencesHelper();
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

    @Override
    public int getItemCount() {
        if (rssSources == null) {
            return 0;
        }
        return rssSources.size();
    }

    private void onRemoveClicked(RssChannel rssChannel, int position) {
        rssSources.remove(position);
        feedStorage.addRssChannelToRemove(rssChannel);
        notifyDataSetChanged();
        if (listener != null && preferencesHelper.hasSeenFeedRemovalWarningDialog(context)) {
            listener.onChannelRemoved(rssChannel.getTitle());
        } else if (!preferencesHelper.hasSeenFeedRemovalWarningDialog(context)){
            showWarningDialog(rssChannel.getTitle());
            preferencesHelper.setSeenFeedRemovalWarningDialog(context, true);
        }
    }

    private void showWarningDialog(String rssTitle) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage(R.string.feeds_unsubscribe)
                .setPositiveButton(R.string.action_ok, (dialogInterface, i) -> {
                    if (listener != null) {
                        listener.onChannelRemoved(rssTitle);
                    }
                })
                .setNegativeButton(R.string.action_cancel, (dialogInterface, i) -> {})
                .create();
        dialogBuilder.show();
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
        void onChannelRemoved(String rssChannelTitle);
    }
}

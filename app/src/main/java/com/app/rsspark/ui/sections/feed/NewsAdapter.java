package com.app.rsspark.ui.sections.feed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.rsspark.R;
import com.app.rsspark.domain.models.NewsItem;
import com.app.rsspark.utils.FormattingUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by konstie on 10.12.16.
 */

public class NewsAdapter extends RealmRecyclerViewAdapter<NewsItem, NewsAdapter.NewsHolder> {
    private static final String TAG = "NewsAdapter";

    private RealmResults<NewsItem> newsItems;

    public NewsAdapter(@NonNull Context context, @Nullable RealmResults<NewsItem> newsItems, boolean autoUpdate) {
        super(context, newsItems, autoUpdate);
        this.newsItems = newsItems;
        Log.w(TAG, "NewsAdapter created");
    }

    public void setNewsItems(RealmResults<NewsItem> newsItems) {
        this.newsItems = newsItems;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        NewsItem newsItem = newsItems.get(position);
        holder.titleTextView.setText(newsItem.getTitle());
        holder.descriptionTextView.setText(newsItem.getDescription());
//        holder.dateTextView.setText(FormattingUtils.getFormattedDate(newsItem.getDate()));
        if (newsItem.getImageUrl() != null && !newsItem.getImageUrl().isEmpty()) {
            holder.newsHeaderImageView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(newsItem.getImageUrl())
                    .into(holder.newsHeaderImageView);
        } else {
            holder.newsHeaderImageView.setVisibility(View.GONE);
        }
    }

    static class NewsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_header_image_view)
        ImageView newsHeaderImageView;
        @BindView(R.id.news_title_text_view)
        TextView titleTextView;
        @BindView(R.id.news_description_text_view)
        TextView descriptionTextView;
        @BindView(R.id.news_date_text_view)
        TextView dateTextView;

        NewsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

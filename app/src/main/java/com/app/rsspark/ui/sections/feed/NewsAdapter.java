package com.app.rsspark.ui.sections.feed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
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

    private OnItemClickListener clickListener;
    private RealmResults<NewsItem> newsItems;

    public NewsAdapter(@NonNull Context context, @Nullable RealmResults<NewsItem> newsItems, boolean autoUpdate, OnItemClickListener clickListener) {
        super(context, newsItems, autoUpdate);
        this.newsItems = newsItems;
        this.clickListener = clickListener;
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
        activateHyperlinksForTextView(holder.descriptionTextView, newsItem.getDescription());
        holder.dateTextView.setText(FormattingUtils.getFormattedDate(newsItem.getRawDate()));
        if (newsItem.getImageUrl() != null && !newsItem.getImageUrl().isEmpty()) {
            holder.newsHeaderImageView.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(newsItem.getImageUrl())
                    .into(holder.newsHeaderImageView);
        } else {
            holder.newsHeaderImageView.setVisibility(View.GONE);
        }
        holder.layoutRoot.setOnClickListener(view -> {
            if (clickListener != null && newsItem.getLink() != null && !newsItem.getLink().isEmpty()) {
                clickListener.onLinkSelected(newsItem.getLink(), newsItem.getTitle());
            }
        });
    }

    private void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span) {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                Log.w(TAG, "Clicked on a link: " + span.getURL());
                clickListener.onLinkSelected(span.getURL(), null);
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    private void activateHyperlinksForTextView(TextView textView, String html) {
        Log.w(TAG, "activateHyperlinksForTextView called for " + html);
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        textView.setText(strBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }


    static class NewsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_root)
        CardView layoutRoot;
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

    public interface OnItemClickListener {
        void onLinkSelected(String articleUrl, String articleTitle);
    }
}

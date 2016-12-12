package com.app.rsspark.ui.sections.article;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.rsspark.R;
import com.app.rsspark.domain.contract.RSSParkConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kmikhailovskiy on 12.12.2016.
 */

public class ArticleActivity extends AppCompatActivity {
    @BindView(R.id.web_view) WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        if (getIntent() != null) {
            String url = getIntent().getStringExtra(RSSParkConstants.EXTRA_ARTICLE_URL);
            String title = getIntent().getStringExtra(RSSParkConstants.EXTRA_ARTICLE_TITLE);
            updateToolbarWithArticleTitle(title);
            webView.loadUrl(url);
        }
    }

    private void updateToolbarWithArticleTitle(String articleTitle) {
        if (articleTitle == null) {
            return;
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(articleTitle);
        }
    }
}

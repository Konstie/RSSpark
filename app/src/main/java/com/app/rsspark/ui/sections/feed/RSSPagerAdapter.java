package com.app.rsspark.ui.sections.feed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by konstie on 11.12.16.
 */

public class RSSPagerAdapter extends FragmentStatePagerAdapter {
    private List<Integer> ids;
    private List<String> titles;

    public RSSPagerAdapter(FragmentManager fm, List<Integer> ids, List<String> titles) {
        super(fm);
        this.ids = ids;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return NewsListFragment.newInstance(ids.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return ids.size();
    }
}
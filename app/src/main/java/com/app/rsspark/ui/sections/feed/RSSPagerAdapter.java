package com.app.rsspark.ui.sections.feed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

import eu.inloop.pager.UpdatableFragmentPagerAdapter;

/**
 * Created by konstie on 11.12.16.
 */

public class RSSPagerAdapter extends UpdatableFragmentPagerAdapter {
    private List<String> rssTitles;

    public RSSPagerAdapter(FragmentManager fm, List<String> rssTitles) {
        super(fm);
        this.rssTitles = rssTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return NewsListFragment.newInstance(rssTitles.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        if (rssTitles.contains(object)) return rssTitles.indexOf(object);
        return POSITION_NONE;
    }

    @Override
    public long getItemId(int position) {
        return rssTitles.get(position).hashCode();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return rssTitles.get(position);
    }

    @Override
    public int getCount() {
        return rssTitles.size();
    }

}
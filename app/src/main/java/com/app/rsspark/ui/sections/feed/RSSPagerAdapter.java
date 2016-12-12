package com.app.rsspark.ui.sections.feed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Pair;

import java.util.List;

/**
 * Created by konstie on 11.12.16.
 */

public class RSSPagerAdapter extends FragmentPagerAdapter {
    private List<NewsListFragment> fragments;
    private List<String> rssDetails;
    private FragmentManager fragmentManager;

    public RSSPagerAdapter(FragmentManager fm, List<NewsListFragment> fragments, List<String> rssDetails) {
        super(fm);
        this.fragmentManager = fm;
        this.fragments = fragments;
        this.rssDetails = rssDetails;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public void addNewFragment(String feedDetails) {
        fragments.add(NewsListFragment.newInstance(feedDetails));
        rssDetails.add(feedDetails);
        notifyDataSetChanged();
    }

    public void removeFragment(int position) {
        rssDetails.remove(position);
        fragmentManager.beginTransaction().remove(fragments.get(position)).commitNow();
        fragments.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return rssDetails.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
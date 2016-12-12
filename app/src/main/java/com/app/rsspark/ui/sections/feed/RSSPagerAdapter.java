package com.app.rsspark.ui.sections.feed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by konstie on 11.12.16.
 */

public class RSSPagerAdapter extends FragmentStatePagerAdapter {
    private List<NewsListFragment> fragments;
    private List<String> rssDetails;
    private FragmentManager fragmentManager;
    private int pagesCount;

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

    public int removeFragment(int position) {
        rssDetails.remove(position);
        fragments.remove(position);
        return position;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return rssDetails.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getItemPosition(Object object) {
        int index = fragments.indexOf(object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }
}
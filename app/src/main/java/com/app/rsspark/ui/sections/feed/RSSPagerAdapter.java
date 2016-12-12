package com.app.rsspark.ui.sections.feed;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by konstie on 11.12.16.
 */

public class RSSPagerAdapter extends FragmentStatePagerAdapter {
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
        NewsListFragment newsListFragment = NewsListFragment.newInstance(feedDetails);
        fragments.add(newsListFragment);
        rssDetails.add(feedDetails);
        notifyDataSetChanged();
    }

    public void removeFragment(int position) {
        rssDetails.remove(position);
        fragments.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (fragments.contains(object)) return fragments.indexOf(object);
        return POSITION_NONE;
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
        if (position >= getCount() && object != null) {
            FragmentManager fragmentManager = ((Fragment) object).getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove((Fragment) object);
            fragmentTransaction.commitNow();
        }
    }
}
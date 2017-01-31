package ykooze.ayaseruri.codesslib.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wufeiyang on 16/6/16.
 */
public class ViewPagerViewAdapter<T extends View> extends PagerAdapter {

    private List<T> mViews;
    private List<String> mTitles;

    public ViewPagerViewAdapter(List<T> views) {
        mViews = views;
        mTitles = new ArrayList<>();
    }

    public ViewPagerViewAdapter(List<T> views, List<String> titles) {
        mViews = views;
        mTitles = titles;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}


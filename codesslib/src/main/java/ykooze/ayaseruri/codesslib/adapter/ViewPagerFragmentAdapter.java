package ykooze.ayaseruri.codesslib.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by wufeiyang on 16/5/21.
 */
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<String> mTitles;

    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
        mTitles = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @UiThread
    public void add(Fragment fragment, String title){
        mFragments.add(fragment);
        mTitles.add(title);
        notifyDataSetChanged();
    }

    @UiThread
    public void add(List<Fragment> fragments, List<String> titles){
        mFragments.addAll(fragments);
        mTitles.addAll(titles);
        notifyDataSetChanged();
    }
}

package pro.luxun.luxunanimation.view.activity;

import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.presenter.presenter.MainActivityPresenter;
import pro.luxun.luxunanimation.view.fragment.MainFragment_;
import pro.luxun.luxunanimation.view.fragment.MeFragment_;
import pro.luxun.luxunanimation.view.fragment.TopicFragment_;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements INetCacheData<MainJson> {

    @ViewById(R.id.main_tab)
    TabLayout mTabLayout;
    @ViewById(R.id.main_view_pager)
    ViewPager mViewPager;
    @StringRes(R.string.loading)
    String mLoadingStr;
    @StringRes(R.string.net_error)
    String mNetError;
    @StringRes(R.string.retry)
    String mRetry;
    @StringArrayRes(R.array.main_tab_titles)
    String[] mMainTabTitles;

    private SweetAlertDialog mAlertDialog;
    private MainActivityPresenter mMainActivityPresenter;

    @AfterViews
    void init(){
        mMainActivityPresenter = new MainActivityPresenter(this);

        mMainActivityPresenter.getMainJsonNetSilent();
        mMainActivityPresenter.getMainJsonCache();
    }

    @Override
    public void onStartGetJsonNet() {
        mAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mAlertDialog.setTitleText(mLoadingStr);
        mAlertDialog.show();
    }

    @Override
    public void onGetJsonSuccessNet(MainJson mainJson) {
        if(null != mAlertDialog && mAlertDialog.isShowing()){
            mAlertDialog.dismiss();
        }

        //初始化Fragments
        initMain();
    }

    @Override
    public void onGetJsonErrorNet() {
        if(null != mAlertDialog){
            mAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            mAlertDialog.setTitleText(mNetError);
            mAlertDialog.setConfirmText(mRetry);
            mAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    mAlertDialog.dismiss();
                    mMainActivityPresenter.getMainJsonNet();
                }
            });
        }
    }

    @Override
    public void onGetJsonCacheSuccess(MainJson mainJson) {
        initMain();
    }

    @Override
    public void onGetJsonCacheFailed() {
        mMainActivityPresenter.getMainJsonNet();
    }

    private void initMain(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.add(MeFragment_.builder().build(), mMainTabTitles[0]);
        viewPagerAdapter.add(MainFragment_.builder().build(), mMainTabTitles[1]);
        viewPagerAdapter.add(TopicFragment_.builder().build(), mMainTabTitles[3]);
        mViewPager.setAdapter(viewPagerAdapter);

        mTabLayout.setVisibility(View.VISIBLE);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter{

        private List<Fragment> mFragments;
        private List<String> mTitles;

        public ViewPagerAdapter(FragmentManager fm) {
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
    }
}

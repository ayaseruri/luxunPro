package pro.luxun.luxunanimation.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements IMainActivity{

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivityPresenter = new MainActivityPresenter(this);

        mAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mAlertDialog.setTitleText(mLoadingStr);

        mMainActivityPresenter.getMainJsonCache();
    }

    @Override
    public void onStartGetMainJsonNet() {
        if(null != mAlertDialog){
            mAlertDialog.show();
        }
    }

    @Override
    public void onGetMainJsonSuccessNet() {
        if(null != mAlertDialog && mAlertDialog.isShowing()){
            mAlertDialog.dismiss();
        }

        //初始化Fragments
        initMain();
    }

    @Override
    public void onGetMainJsonErrorNet() {
        if(null != mAlertDialog){
            mAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
            mAlertDialog.setTitleText(mNetError);
            mAlertDialog.setConfirmText(mRetry);
            mAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    mMainActivityPresenter.getMainJsonNet();
                    mAlertDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onGetMainJsonCacheSuccess(MainJson mainJson) {
        if(null != mainJson){
            //初始化Fragments
            initMain();
        }else {
            mMainActivityPresenter.getMainJsonNet();
        }
    }

    @Override
    public void onGetMainJsonCacheFailed() {
        mMainActivityPresenter.getMainJsonNet();
    }

    private void initMain(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.add(new MainFragment_(), mMainTabTitles[0]);
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

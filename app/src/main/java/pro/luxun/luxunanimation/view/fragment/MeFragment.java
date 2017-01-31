package pro.luxun.luxunanimation.view.fragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringRes;

import com.bumptech.glide.Glide;
import com.commit451.nativestackblur.NativeStackBlur;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.Auth;
import pro.luxun.luxunanimation.bean.GetToken;
import pro.luxun.luxunanimation.net.ApiService;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.AuthInfoHelper;
import pro.luxun.luxunanimation.utils.StartUtils;
import pro.luxun.luxunanimation.utils.UserInfoHelper;
import ykooze.ayaseruri.codesslib.adapter.ViewPagerFragmentAdapter;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

/**
 * Created by wufeiyang on 16/5/17.
 */
@EFragment
public class MeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String AUTH_STR = "你为什么这么熟练啊?";
    private static final int POSTION_BANGUMI = 0;
    private static final int POSTION_LIKE_COMMENT = 1;

    @StringRes(R.string.auth_hint)
    String mAuthHint;
    @StringRes(R.string.auth_ing)
    String mAuthIng;
    @StringRes(R.string.auth_failed)
    String mAuthFailed;
    @ColorRes(R.color.colorPrimaryLight)
    int mColorPrimaryLight;

    private ApiService mApiService;
    private String mTmpToken;
    private View mRootView;

    private SwipeRefreshLayout mRefreshLayout;
    private RelativeLayout mRelativeLayout;
    private ImageView mAvatar;
    private ImageView mHeaderImg;
    private Button mLoginBtn;
    private TextView mUserName;
    private TextView mUserDes;
    private MaterialProgressBar mProgressBar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ViewPagerFragmentAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null == mRootView){
            mRootView = inflater.inflate(R.layout.fragment_me, container, false);
            mRelativeLayout = ((RelativeLayout) mRootView.findViewById(R.id.root));
            mAvatar = ((ImageView) mRootView.findViewById(R.id.avatar));
            mHeaderImg = ((ImageView) mRootView.findViewById(R.id.header_img));
            mLoginBtn = ((Button) mRootView.findViewById(R.id.login_btn));
            mUserName = ((TextView) mRootView.findViewById(R.id.user_name));
            mUserDes = ((TextView) mRootView.findViewById(R.id.user_des));
            mProgressBar = ((MaterialProgressBar) mRootView.findViewById(R.id.progress));
            mViewPager = (ViewPager) mRootView.findViewById(R.id.view_pager);
            mTabLayout = (TabLayout) mRootView.findViewById(R.id.tab);
            mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh);
        }

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mApiService.getToken(RetrofitClient.URL_AUTH_JSON, AUTH_STR).compose(RxUtils.<GetToken>applySchedulers())
                    .subscribe(new Observer<GetToken>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mProgressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNext(GetToken getToken) {
                            mTmpToken = getToken.getToken();
                            Snackbar.make(mRelativeLayout, mAuthHint, Snackbar.LENGTH_LONG)
                                    .setAction("认证", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AuthInfoHelper.setRequestAuth(true);
                                            AuthInfoHelper.saveAuthToken(mTmpToken);
                                            StartUtils.startLocalBorwser(mActivity, RetrofitClient.URL_WEB_AUTH + mTmpToken);
                                        }
                                    }).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Snackbar.make(mRelativeLayout, mAuthFailed, Snackbar.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onComplete() {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
            }
        });

        mPagerAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager());

        mPagerAdapter.add(new BangumiFragment(), "我的追番");
        mPagerAdapter.add(new LikeCommentFragment(), "喜欢番评");

        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mApiService = RetrofitClient.getApiService();

        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.colorPrimary));
        mRefreshLayout.setOnRefreshListener(this);

        return mRootView;
    }

    private void initUserInfo(){
        if(UserInfoHelper.isLogin(mActivity)){
            final Auth.UserEntity userEntity = UserInfoHelper.getUserInfo(mActivity);
            Glide.with(mActivity).load(userEntity.getAvatar()).centerCrop().crossFade().into(mAvatar);

            mLoginBtn.setVisibility(View.GONE);

            mUserName.setVisibility(View.VISIBLE);
            mUserDes.setVisibility(View.VISIBLE);
            mUserName.setText(userEntity.getName());
            mUserDes.setText(TextUtils.isEmpty(userEntity.getDes()) ? "暂无简介" : userEntity.getDes());

            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                    Bitmap bitmap = Glide.with(mActivity).load(userEntity.getAvatar()).asBitmap().into(400, 400).get();
                    e.onNext(NativeStackBlur.process(bitmap, 30));
                    e.onComplete();
                }
            }).compose(RxUtils.<Bitmap>applySchedulers()).subscribe(new Consumer<Bitmap>() {
                @Override
                public void accept(Bitmap bitmap) throws Exception {
                    mHeaderImg.setImageBitmap(bitmap);
                }
            });
        }else {
            mLoginBtn.setVisibility(View.VISIBLE);
            mHeaderImg.setBackgroundColor(mColorPrimaryLight);
            mAvatar.setImageResource(R.mipmap.ic_default_avatar);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AuthInfoHelper.isRequestAuth()){
            AuthInfoHelper.setRequestAuth(false);
            mTmpToken = AuthInfoHelper.getAuthToken();
            Snackbar.make(mRelativeLayout, mAuthIng, Snackbar.LENGTH_LONG).show();
            mApiService.auth(RetrofitClient.URL_AUTH_JSON + mTmpToken).compose(RxUtils.<Auth>applySchedulers())
                    .subscribe(new Observer<Auth>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mProgressBar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNext(Auth auth) {
                            UserInfoHelper.save(mActivity, auth.getUser());
                            onRefresh();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mProgressBar.setVisibility(View.GONE);
                            Snackbar.make(mRelativeLayout, mAuthFailed, Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onComplete() {
                            mProgressBar.setVisibility(View.GONE);
                        }
                    });
        }else {
            initUserInfo();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
    }

    @Override
    public void onRefresh() {
        initUserInfo();
        ((BangumiFragment)mPagerAdapter.instantiateItem(mViewPager, POSTION_BANGUMI))
                .refresh(new BangumiFragment.IOnRefreshComplete() {
                    @Override
                    public void onComplete() {
                        mRefreshLayout.setRefreshing(false);
                    }
                });

        ((LikeCommentFragment)mPagerAdapter.instantiateItem(mViewPager, POSTION_LIKE_COMMENT))
                .refresh(new LikeCommentFragment.IOnRefreshComplete() {
                    @Override
                    public void onComplete() {
                        mRefreshLayout.setRefreshing(false);
                    }
                });
    }
}

package pro.luxun.luxunanimation.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.commit451.nativestackblur.NativeStackBlur;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.ExecutionException;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.Auth;
import pro.luxun.luxunanimation.bean.GetToken;
import pro.luxun.luxunanimation.net.ApiService;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.presenter.adapter.ViewPagerAdapter;
import pro.luxun.luxunanimation.utils.AuthInfoHelper;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.utils.StartUtils;
import pro.luxun.luxunanimation.utils.UserInfoHelper;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by wufeiyang on 16/5/17.
 */
@EFragment
public class MeFragment extends BaseFragment {

    private static final String AUTH_STR = "Hello,this is Android!";

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

    private RelativeLayout mRelativeLayout;
    private ImageView mAvatar;
    private ImageView mHeaderImg;
    private Button mLoginBtn;
    private TextView mUserName;
    private TextView mUserDes;
    private MaterialProgressBar mProgressBar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

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
        }

        if (this.mLoginBtn!= null) {
            this.mLoginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mApiService.getToken(RetrofitClient.URL_AUTH_JSON, AUTH_STR).compose(RxUtils.<GetToken>applySchedulers())
                            .subscribe(new Subscriber<GetToken>() {
                                @Override
                                public void onCompleted() {
                                    mProgressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Snackbar.make(mRelativeLayout, mAuthFailed, Snackbar.LENGTH_LONG).show();
                                    mProgressBar.setVisibility(View.GONE);
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
                                public void onStart() {
                                    mProgressBar.setVisibility(View.VISIBLE);
                                }
                            });
                }
            });
        }

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.add(BangumiFragment_.builder().build(), "我的追番");
        pagerAdapter.add(LikeCommentFragment_.builder().build(), "喜欢番评");

        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mApiService = RetrofitClient.getApiService();

        return mRootView;
    }

    private void initUserHeader(){
        if(UserInfoHelper.isLogin()){
            Auth.UserEntity userEntity = UserInfoHelper.getUserInfo();
            final String avatarUrl = userEntity.getAvatar();
            Glide.with(mActivity).load(avatarUrl).centerCrop().crossFade().into(mAvatar);

            mLoginBtn.setVisibility(View.GONE);

            mUserName.setVisibility(View.VISIBLE);
            mUserDes.setVisibility(View.VISIBLE);
            mUserName.setText(userEntity.getName());
            mUserDes.setText(TextUtils.isEmpty(userEntity.getDes()) ? "暂无简介" : userEntity.getDes());

            Observable.create(new Observable.OnSubscribe<Bitmap>() {
                @Override
                public void call(Subscriber<? super Bitmap> subscriber) {
                    try {
                        Bitmap bitmap = Glide.with(mActivity).load(avatarUrl).asBitmap().into(400, 400).get();
                        subscriber.onNext(NativeStackBlur.process(bitmap, 30));
                        subscriber.onCompleted();
                    } catch (InterruptedException e) {
                        subscriber.onError(e);
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        subscriber.onError(e);
                        e.printStackTrace();
                    }
                }
            }).compose(RxUtils.<Bitmap>applySchedulers()).subscribe(new Action1<Bitmap>() {
                @Override
                public void call(Bitmap bitmap) {
                    mHeaderImg.setImageBitmap(bitmap);
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {

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
                    .subscribe(new Subscriber<Auth>() {
                        @Override
                        public void onCompleted() {
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mProgressBar.setVisibility(View.GONE);
                            Snackbar.make(mRelativeLayout, mAuthFailed, Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNext(Auth auth) {
                            UserInfoHelper.save(auth.getUser());
                            initUserHeader();
                        }

                        @Override
                        public void onStart() {
                            mProgressBar.setVisibility(View.VISIBLE);
                        }
                    });
        }else {
            initUserHeader();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
    }
}

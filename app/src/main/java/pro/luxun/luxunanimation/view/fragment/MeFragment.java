package pro.luxun.luxunanimation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.widget.FrameLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.Auth;
import pro.luxun.luxunanimation.bean.GetToken;
import pro.luxun.luxunanimation.net.ApiService;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.EventBusMsg;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.utils.StartUtils;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by wufeiyang on 16/5/17.
 */
@EFragment(R.layout.fragment_me)
public class MeFragment extends BaseFragment {

    private static final String AUTH_STR = "Hello,this is Android!";

    @ViewById(R.id.root)
    FrameLayout mFrameLayout;

    private ApiService mApiService;
    private String mTmpToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @AfterViews
    void init(){
        mApiService = RetrofitClient.getApiService();
    }

    @Click(R.id.login_btn)
    void onLogin(){
        mApiService.getToken(RetrofitClient.URL_AUTH_JSON, AUTH_STR).compose(RxUtils.<GetToken>applySchedulers())
                .subscribe(new Action1<GetToken>() {
                    @Override
                    public void call(GetToken getToken) {
                        mTmpToken = getToken.getToken();
                        StartUtils.startLocalBorwser(mActivity, RetrofitClient.URL_WEB_AUTH + mTmpToken);
                        Snackbar.make(mFrameLayout, "请在稍后打开的页面内完成认证XD", Snackbar.LENGTH_LONG).show();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    @Subscribe
    public void onResult(EventBusMsg.MsgAuthBack msgAuthBack){
        if(!TextUtils.isEmpty(mTmpToken)){
            mApiService.auth(RetrofitClient.URL_AUTH_JSON + mTmpToken).compose(RxUtils.<Auth>applySchedulers())
                    .subscribe(new Subscriber<Auth>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Auth auth) {

                        }
                    });
        }
    }
}

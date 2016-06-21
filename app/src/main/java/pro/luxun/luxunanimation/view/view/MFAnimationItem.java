package pro.luxun.luxunanimation.view.view;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import okhttp3.RequestBody;
import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.LikeBangumi;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.global.MApplication;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.LocalDisplay;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.utils.StartUtils;
import pro.luxun.luxunanimation.utils.UserInfoHelper;
import pro.luxun.luxunanimation.utils.Utils;
import rx.Subscriber;

/**
 * Created by wufeiyang on 16/5/7.
 */

@EViewGroup(R.layout.item_animation_mf)
public class MFAnimationItem extends FrameLayout{

    @ViewById(R.id.disable_mask)
    ImageView mMask;
    @ViewById(R.id.cover)
    ImageView mCover;
    @ViewById(R.id.cur)
    TextView mCur;
    @ViewById(R.id.title)
    TextView mTitle;
    @ViewById(R.id.favrite)
    CheckBox mFavrite;
    @App
    MApplication mMApplication;

    public MFAnimationItem(Context context) {
        super(context);
    }

    public MFAnimationItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        int width = (LocalDisplay.SCREEN_WIDTH_PIXELS - LocalDisplay.dp2px(4)) / 3;
        setLayoutParams(new ViewGroup.LayoutParams( width, (int) (width / 3 * 4.5)));
    }

    public void bind(final MainJson.UpdatingEntity updatingEntity, String keywords){
        mCur.setText(updatingEntity.getCur());
        final String title = updatingEntity.getTitle();
        mTitle.setText(title);

        if(TextUtils.isEmpty(keywords)){
            mMask.setVisibility(GONE);
        }else {
            mMask.setVisibility(title.contains(keywords) ? GONE : VISIBLE);
        }

        Glide.with(getContext()).load(updatingEntity.getCover()).centerCrop().crossFade().into(mCover);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updatingEntity.setSub(mFavrite.isChecked());
                StartUtils.startAnimationDetailActivity(getContext(), updatingEntity);
            }
        });

        mFavrite.setOnCheckedChangeListener(null);
        mFavrite.setChecked(updatingEntity.isSub());

        mFavrite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {

                if(!UserInfoHelper.isLogin()){
                    Snackbar.make(getRootView(), "需要登录TAT…", Snackbar.LENGTH_LONG).setAction("登录", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StartUtils.startMainActivity(getContext(), 0);
                        }
                    }).show();
                    mFavrite.setChecked(!isChecked);
                    return;
                }

                RequestBody requestBody;
                if(isChecked){
                    requestBody = Utils.str2RequestBody("1");
                }else {
                    requestBody = Utils.str2RequestBody("0");
                }

                updatingEntity.setSub(isChecked);

                RetrofitClient.getApiService().subscribe(RetrofitClient.URL_BANGUMI + Utils.encodeURIComponent(title), requestBody)
                        .compose(RxUtils.<LikeBangumi>applySchedulers())
                        .subscribe(new Subscriber<LikeBangumi>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mMApplication.showToast(isChecked ?"订阅 " : "取消订阅 " + title + " 失败,请稍后重试", MApplication.TOAST_ALERT);
                                mFavrite.setChecked(!isChecked);
                                updatingEntity.setSub(mFavrite.isChecked());
                            }

                            @Override
                            public void onNext(LikeBangumi likeBangumi) {
                                if(TextUtils.isEmpty(likeBangumi.getType())){
                                    mFavrite.setChecked(!isChecked);
                                }
                            }
                        });
            }
        });
    }
}

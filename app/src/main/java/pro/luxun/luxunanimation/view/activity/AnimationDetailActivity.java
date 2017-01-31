package pro.luxun.luxunanimation.view.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DimensionRes;
import org.androidannotations.annotations.res.StringRes;

import com.bumptech.glide.Glide;
import com.commit451.nativestackblur.NativeStackBlur;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import okhttp3.RequestBody;
import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.LikeBangumi;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.global.IntentConstant;
import pro.luxun.luxunanimation.global.MApplication;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.StartUtils;
import pro.luxun.luxunanimation.utils.TranslucentStatusHelper;
import pro.luxun.luxunanimation.utils.UserInfoHelper;
import pro.luxun.luxunanimation.utils.Utils;
import pro.luxun.luxunanimation.view.view.AnimationSets;
import pro.luxun.luxunanimation.view.view.VideoComment;
import ykooze.ayaseruri.codesslib.rx.RxActivity;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

@EActivity(R.layout.activity_animation_detail)
public class AnimationDetailActivity extends RxActivity {

    @ViewById(R.id.observable_scroll_view)
    ObservableScrollView mScrollView;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.status_bar)
    ImageView mStatusbar;
    @ViewById(R.id.header_img)
    ImageView mHeaderImg;
    @ViewById(R.id.cover)
    ImageView mCover;
    @ViewById(R.id.title)
    TextView mTitle;
    @ViewById(R.id.info)
    TextView mInfo;
    @ViewById(R.id.animation_sets)
    AnimationSets mAnimationSets;
    @ViewById(R.id.introduce)
    TextView mIntroduce;
    @ViewById(R.id.video_comment)
    VideoComment mVideoComment;
    @ViewById(R.id.favrite)
    FloatingActionButton mActionButton;
    @App
    MApplication mMApplication;

    @ColorRes(R.color.colorPrimary)
    int mColorPrimary;
    @DimensionRes(R.dimen.detail_head_height)
    float mDetailHeadHeight;
    @StringRes(R.string.detail_head_info)
    String mDetailHeadInfo;

    private MainJson.UpdatingEntity mUpdatingEntity;

    @AfterViews
    void init(){
        mUpdatingEntity = getIntent().getParcelableExtra(IntentConstant.INTENT_UPDATING_ENTITY);

        initToolbar();
        initTranslucentStatus();
        initHeaderBlur(mUpdatingEntity.getCover());

        Glide.with(this).load(mUpdatingEntity.getCover()).centerCrop().into(mCover);

        mTitle.setText(mUpdatingEntity.getTitle());
        mInfo.setText(String.format(mDetailHeadInfo
                , 0 == mUpdatingEntity.getWeek() ? "日" : Utils.num2Str(mUpdatingEntity.getWeek())
                , mUpdatingEntity.getSets().size()));

        for(MainJson.UpdatingEntity.SetsEntity setsEntity : mUpdatingEntity.getSets()){
            setsEntity.setTitle(mUpdatingEntity.getTitle());
            setsEntity.setOrgTitle(mUpdatingEntity.getOriginal());
        }
        mAnimationSets.init(mUpdatingEntity.getSets());

        mIntroduce.setText(mUpdatingEntity.getText());

        mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mColorPrimary));
        mStatusbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mColorPrimary));

        mVideoComment.initComment(mUpdatingEntity.getTitle());

        mScrollView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(Math.min(1, scrollY / mDetailHeadHeight), mColorPrimary));
                mStatusbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(Math.min(1, scrollY / mDetailHeadHeight), mColorPrimary));
            }

            @Override
            public void onDownMotionEvent() {

            }

            @Override
            public void onUpOrCancelMotionEvent(ScrollState scrollState) {

            }
        });

        mActionButton.setSelected(mUpdatingEntity.isSub());
    }

    @Click(R.id.favrite)
    void onActionBtn(){
        if(!UserInfoHelper.isLogin(this)){
            Snackbar.make(mScrollView, "请先登录…", Snackbar.LENGTH_LONG).setAction("登录", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StartUtils.startMainActivity(AnimationDetailActivity.this, 0);
                }
            }).show();
            return;
        }

        RequestBody requestBody;
        if(!mUpdatingEntity.isSub()){
            requestBody = Utils.str2RequestBody("1");
        }else {
            requestBody = Utils.str2RequestBody("0");
        }

        mUpdatingEntity.setSub(!mUpdatingEntity.isSub());
        mActionButton.setSelected(mUpdatingEntity.isSub());

        RetrofitClient.getApiService().subscribe(RetrofitClient.URL_BANGUMI + Utils.encodeURIComponent(mUpdatingEntity.getTitle()), requestBody)
                .compose(RxUtils.<LikeBangumi>applySchedulers())
                .subscribe();
    }

    private void initTranslucentStatus(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            TranslucentStatusHelper.translucentStatus(this);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mStatusbar.getLayoutParams();
            layoutParams.height = TranslucentStatusHelper.getStatusBarHeight(this);
        }
    }

    private void initToolbar(){
        setTitle("详情");
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(null != getSupportActionBar()){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initHeaderBlur(final String url){
        Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                Bitmap bitmap = Glide.with(AnimationDetailActivity.this)
                        .load(url).asBitmap().into(400, 400).get();
                bitmap = NativeStackBlur.process(bitmap, 30);
                e.onNext(bitmap);
                e.onComplete();
            }
        }).compose(RxUtils.<Bitmap>applySchedulers())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        mHeaderImg.setImageBitmap(bitmap);
                    }
                });
    }
}

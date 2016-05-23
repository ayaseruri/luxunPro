package pro.luxun.luxunanimation.view.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.commit451.nativestackblur.NativeStackBlur;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DimensionRes;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.ExecutionException;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.global.IntentConstant;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.utils.TranslucentStatusHelper;
import pro.luxun.luxunanimation.utils.Utils;
import pro.luxun.luxunanimation.view.view.AnimationSets;
import pro.luxun.luxunanimation.view.view.VideoComment;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;


@EActivity(R.layout.activity_animation_detail)
public class AnimationDetailActivity extends AppCompatActivity {

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

    @ColorRes(R.color.colorPrimary)
    int mColorPrimary;
    @DimensionRes(R.dimen.detail_head_height)
    float mDetailHeadHeight;
    @StringRes(R.string.detail_head_info)
    String mDetailHeadInfo;

    @AfterViews
    void init(){
        MainJson.UpdatingEntity updatingEntity = getIntent().getParcelableExtra(IntentConstant.INTENT_UPDATING_ENTITY);

        initToolbar();
        initTranslucentStatus();
        initHeaderBlur(updatingEntity.getCover());

        Glide.with(this).load(updatingEntity.getCover()).centerCrop().into(mCover);

        mTitle.setText(updatingEntity.getTitle());
        mInfo.setText(String.format(mDetailHeadInfo, 0 == updatingEntity.getWeek() ? "日" : Utils.num2Str(updatingEntity.getWeek()), updatingEntity.getSets().size()));

        for(MainJson.UpdatingEntity.SetsEntity setsEntity : updatingEntity.getSets()){
            setsEntity.setTitle(updatingEntity.getTitle());
            setsEntity.setOrgTitle(updatingEntity.getOriginal());
        }
        mAnimationSets.init(updatingEntity.getSets());

        mIntroduce.setText(updatingEntity.getText());

        mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mColorPrimary));
        mStatusbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mColorPrimary));

        mVideoComment.initComment(updatingEntity.getTitle());

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
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    Bitmap bitmap = Glide.with(AnimationDetailActivity.this)
                            .load(url).asBitmap().into(400, 400).get();
                    bitmap = NativeStackBlur.process(bitmap, 30);
                    subscriber.onNext(bitmap);
                    subscriber.onCompleted();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
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
    }
}

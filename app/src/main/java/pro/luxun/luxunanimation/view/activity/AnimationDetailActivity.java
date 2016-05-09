package pro.luxun.luxunanimation.view.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.global.IntentConstant;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.utils.TranslucentStatusHelper;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;


@EActivity(R.layout.activity_animation_detail)
public class AnimationDetailActivity extends AppCompatActivity {

    @ViewById(R.id.observable_scroll_view)
    ObservableScrollView mScrollView;
    @ColorRes(R.color.colorPrimary)
    int mColorPrimary;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.status_bar)
    ImageView mStatusbar;
    @ViewById(R.id.header_root)
    FrameLayout mHeaderRoot;
    @ViewById(R.id.header_img)
    ImageView mHeaderImg;

    @DimensionRes(R.dimen.detail_head_height)
    float mDetailHeadHeight;

    @AfterViews
    void init(){
        MainJson.UpdatingEntity updatingEntity = getIntent().getParcelableExtra(IntentConstant.INTENT_UPDATING_ENTITY);

        initTranslucentStatus();
        initHeaderBlur(updatingEntity.getCover());

        mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mColorPrimary));

        mScrollView.setScrollViewCallbacks(new ObservableScrollViewCallbacks() {
            @Override
            public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(Math.max(1, scrollY / mDetailHeadHeight), mColorPrimary));
                mStatusbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(Math.max(1, scrollY / mDetailHeadHeight), mColorPrimary));
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
            mStatusbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mColorPrimary));
        }
    }

    private void initHeaderBlur(final String url){
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    Bitmap bitmap = Glide.with(AnimationDetailActivity.this)
                            .load(url).asBitmap().into(400, 400).get();
                    bitmap = NativeStackBlur.process(bitmap, 40);
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

    private String UrlEncode(String s){
        try {
            return URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}

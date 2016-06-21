package pro.luxun.luxunanimation.view.view.Viedo;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.media.MediaCodec;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.exoplayer.BuildConfig;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.util.PlayerControl;
import com.nineoldandroids.animation.Animator;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.SeekBarTouchStart;
import org.androidannotations.annotations.SeekBarTouchStop;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;
import java.util.concurrent.TimeUnit;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.Danmaku;
import pro.luxun.luxunanimation.global.MApplication;
import pro.luxun.luxunanimation.global.MApplication_;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.DanmakuSettingHelper;
import pro.luxun.luxunanimation.utils.JsonUtils;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.utils.SimpleAnimationListener;
import pro.luxun.luxunanimation.utils.Utils;
import pro.luxun.luxunanimation.view.view.Danmaku.DanmakuSetting;
import pro.luxun.luxunanimation.view.view.Danmaku.DanmakuView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by wufeiyang on 16/5/9.
 */

@EViewGroup(R.layout.video)
public class VideoView extends FrameLayout implements ExoPlayer.Listener, CompoundButton.OnCheckedChangeListener {

    private static final int K = 1024;
    private static final int TIME_ANIMATION = 250;

    private MediaCodecVideoTrackRenderer mVideoRender;
    private MediaCodecAudioTrackRenderer mAudioRender;
    private ExoPlayer mPlayer = ExoPlayer.Factory.newInstance(2);

    private PlayerControl mPlayerControl;
    private Subscription mUiTimer;
    private String userAgent, mDanmakuType, mDanmakuUrl;

    private boolean isHudConutDis = false;
    private int mHudVisableTime = 10, mDanmakuColor;
    private AppCompatActivity mActivity;
    private TelephonyManager mTelephoneManager;

    @ViewById(R.id.surface_view)
    SurfaceView mSurfaceView;
    @ViewById(R.id.progress)
    ProgressWheel mProgressWheel;
    @ViewById(R.id.hud)
    RelativeLayout mHud;
    @ViewById(R.id.seek_bar)
    AppCompatSeekBar mSeekBar;
    @ViewById(R.id.time)
    TextView mTime;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.danmaku)
    DanmakuView mDanmakuView;
    @ViewById(R.id.danmaku_et)
    EditText mDanmakuET;
    @ViewById(R.id.danmaku_setting)
    ImageView mDanmakuSettingIV;
    @ViewById(R.id.submit_btn)
    ImageButton mSubmitBtn;
    @ViewById(R.id.danmaku_switch)
    SwitchCompat mDanmakuSwitch;
    @ViewById(R.id.play_btn)
    CheckBox mPlayBtn;

    @App
    MApplication mMApplication;
    @StringRes(R.string.video_time)
    String mVideoTime;
    @Bean
    DanmakuSetting mDanmakuSetting;

    public VideoView(Context context) {
        super(context);
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        setKeepScreenOn(true);
        mTelephoneManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        mTelephoneManager.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state){
                    case TelephonyManager.CALL_STATE_RINGING:
                        mPlayerControl.pause();
                        mDanmakuView.pause();
                        break;
                    default:
                        break;
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);

        userAgent = "android/okhttp" + BuildConfig.VERSION_NAME;

        mPlayer.addListener(this);
        mPlayerControl = new PlayerControl(mPlayer);

        mActivity = (AppCompatActivity) getContext();

        mDanmakuColor = mDanmakuSetting.getDefaultColor();
        mDanmakuType = mDanmakuSetting.getDefaultType();

        mDanmakuSetting.setIOnColorPaletteClick(new DanmakuSetting.IOnColorPaletteClick() {
            @Override
            public void onColorClick(String type, int color) {
                mDanmakuType = type;
                mDanmakuColor = color;
                mDanmakuSettingIV.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            }
        });
        mDanmakuSwitch.setChecked(DanmakuSettingHelper.getSwitchStatus());

        mSubmitBtn.setEnabled(false);

        mPlayBtn.setOnCheckedChangeListener(this);

        mDanmakuView.pause();
        mDanmakuET.setImeActionLabel("biu~", EditorInfo.IME_ACTION_DONE);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState){
            case PlaybackState.STATE_PLAYING:
                mProgressWheel.setVisibility(GONE);
                mDanmakuView.resume();
                startUitimer();
                hideSystemUI();
                mPlayBtn.setOnCheckedChangeListener(null);
                mPlayBtn.setChecked(false);
                mPlayBtn.setOnCheckedChangeListener(this);
                break;
            case PlaybackState.STATE_PAUSED:
                mPlayBtn.setOnCheckedChangeListener(null);
                mPlayBtn.setChecked(true);
                mPlayBtn.setOnCheckedChangeListener(this);
                break;
            case PlaybackState.STATE_BUFFERING:
                mProgressWheel.setVisibility(VISIBLE);
                break;
            case PlaybackState.STATE_STOPPED:
                break;
            case PlaybackState.STATE_ERROR:
                MApplication_.getInstance().showToast("播放出现问题", MApplication.TOAST_ALERT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPlayWhenReadyCommitted() {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @CheckedChange(R.id.danmaku_switch)
    void onDanmakuSwitch(CompoundButton switchBtn, boolean isChecked){
        mDanmakuView.setVisibility(isChecked ? VISIBLE : GONE);
    }

    @EditorAction(R.id.danmaku_et)
    void onDanmakuETAction(TextView danmakuET, int actionId){
        if(actionId == EditorInfo.IME_ACTION_DONE){
            onSubmitDanmaku();
        }
    }

    @FocusChange(R.id.danmaku_et)
    void onDanmakuETFocus(View danmakuET, boolean hasFocus){
        if(hasFocus){
            mPlayerControl.pause();
            mDanmakuView.pause();
            mPlayBtn.setOnCheckedChangeListener(null);
            mPlayBtn.setChecked(true);
            mPlayBtn.setOnCheckedChangeListener(this);
            stopUiTimer();
        }else {
            mPlayerControl.start();
            mDanmakuView.resume();
            mPlayBtn.setOnCheckedChangeListener(null);
            mPlayBtn.setChecked(false);
            mPlayBtn.setOnCheckedChangeListener(this);
            startUitimer();
        }
    }

    @SeekBarProgressChange(R.id.seek_bar)
    void onSeekBarProgress(){
        mTime.setText(String.format(mVideoTime, Utils.videoTimeFormat(mSeekBar.getProgress())
                , Utils.videoTimeFormat(mPlayer.getDuration())));
    }

    @SeekBarTouchStart(R.id.seek_bar)
    void onSeekBarTouchStart(){
        Log.d("touch", "seek_bar_touch_start");
        stopUiTimer();
    }

    @SeekBarTouchStop(R.id.seek_bar)
    void onSeekBarTouchStop(){
        Log.d("touch", "seek_bar_touch_stop");
        mPlayer.seekTo(mSeekBar.getProgress());
        mDanmakuView.seekTo(Long.valueOf(mSeekBar.getProgress()));
        startUitimer();
    }

    @Click(R.id.danmaku_setting)
    void onDanmakuSetting(){
        mDanmakuSetting.show(mDanmakuSettingIV);
    }

    @Click(R.id.submit_btn)
    void onSubmitDanmaku(){
        String danmakuStr = mDanmakuET.getText().toString();
        if(TextUtils.isEmpty(danmakuStr)){
            YoYo.with(Techniques.Shake).playOn(mDanmakuET);
            return;
        }

        RetrofitClient.getApiService().submitDm(mDanmakuUrl
                , Utils.str2RequestBody(String.valueOf(mPlayerControl.getCurrentPosition() / 1000))
                , Utils.str2RequestBody(danmakuStr)
                , Utils.str2RequestBody(String.format("#%06X", 0xFFFFFF & mDanmakuColor))
                , 19
                , Utils.str2RequestBody(mDanmakuType)).compose(RxUtils.<Danmaku>applySchedulers()).subscribe(new Subscriber<Danmaku>() {
            @Override
            public void onCompleted() {
                if (null != mActivity.getCurrentFocus()) {
                    ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken()
                                    , InputMethodManager.HIDE_NOT_ALWAYS);
                }
                mDanmakuET.clearFocus();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mMApplication.showToast("弹幕发送失败", MApplication.TOAST_ALERT);
            }

            @Override
            public void onNext(Danmaku danmaku) {
                mDanmakuView.addDanmaku(danmaku);
                mDanmakuET.setText("");
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("touch", "dispatch_down");
                if(mHud.getVisibility() == INVISIBLE){
                    isHudConutDis = false;
                    mHudVisableTime = 10;
                    showSystemUI();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("touch", "dispatch_cancel");
                isHudConutDis = true;
                break;
            case MotionEvent.ACTION_UP:
                Log.d("touch", "dispatch_up");
                isHudConutDis = true;
                break;
            default:
                break;
        }
        return true;
    }

    private void startUitimer(){
        if(null != mUiTimer && !mUiTimer.isUnsubscribed()){
            return;
        }
        mUiTimer = Observable.interval(1, TimeUnit.SECONDS).compose(RxUtils.applySchedulers())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        mSeekBar.setMax((int) mPlayer.getDuration());
                        mSeekBar.setProgress((int) mPlayer.getCurrentPosition());

                        if(isHudConutDis && mHudVisableTime-- < 0){
                            isHudConutDis = false;
                            hideSystemUI();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    private void stopUiTimer(){
        if(null != mUiTimer && !mUiTimer.isUnsubscribed()){
            mUiTimer.unsubscribe();
        }
    }

    public void initPlayer(String title, String url){
        Log.d("video_url", url);

        mToolbar.setTitle(title);
        mActivity.setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        ActionBar actionBar = mActivity.getSupportActionBar();
        if(null != actionBar){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        showSystemUI();

        initRender(url);
    }

    public void initDanmaku(String title, String cur){
        mDanmakuUrl = RetrofitClient.URL_DM + Utils.encodeURIComponent("lx:" + title + cur);
        RetrofitClient.getApiService().getDm(mDanmakuUrl)
                .compose(RxUtils.<List<List>>applySchedulers())
                .subscribe(new Subscriber<List<List>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<List> danmakulists) {
                        mDanmakuView.refreshDanmaku(JsonUtils.parserDanmaku(danmakulists));
                    }
                });
        mSubmitBtn.setEnabled(true);
    }

    private void showSystemUI(){
        if(INVISIBLE == mHud.getVisibility()){
            mHud.setVisibility(VISIBLE);
            YoYo.with(Techniques.FadeIn).duration(TIME_ANIMATION).playOn(mHud);
            mActivity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void hideSystemUI(){
        if(VISIBLE == mHud.getVisibility()){
            YoYo.with(Techniques.FadeOut).duration(TIME_ANIMATION).withListener(new SimpleAnimationListener(){
                @Override
                public void onAnimationEnd(Animator animation) {
                    mHud.setVisibility(INVISIBLE);
                }
            }).playOn(mHud);
            mActivity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    private void initRender(String url){
        Uri uri = Uri.parse(url);
        OkHttpDataSource okHttpDataSource = new OkHttpDataSource(RetrofitClient.getOkHttpClient()
                ,userAgent , null);
        okHttpDataSource.setRequestProperty("Referer", RetrofitClient.URL_REFERER);

        ExtractorSampleSource sampleSource = new ExtractorSampleSource(uri, okHttpDataSource, new DefaultAllocator(5 * K), 5 * K * K, new Mp4Extractor());


        mVideoRender = new MediaCodecVideoTrackRenderer(sampleSource, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT, 5 * K);
        mAudioRender = new MediaCodecAudioTrackRenderer(sampleSource);

        mPlayer.sendMessage(mVideoRender, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, mSurfaceView.getHolder().getSurface());
        mPlayer.setPlayWhenReady(true);
    }

    public void pausePlayer(){
        mPlayerControl.pause();
        mDanmakuView.pause();
        stopUiTimer();
    }

    public void resumePlayer(){
        mPlayer.prepare(mVideoRender, mAudioRender);
        mDanmakuView.resume();
        startUitimer();
    }

    public void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

        if(mDanmakuView != null){
            mDanmakuView.release();
            mDanmakuView = null;
        }
        stopUiTimer();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Activity activity = (Activity) getContext();
        View view = activity.getCurrentFocus();
        if (null != view) {
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            mDanmakuET.clearFocus();
        }
        if(isChecked){
            mPlayerControl.pause();
            mDanmakuView.pause();
        }else {
            mPlayerControl.start();
            mDanmakuView.resume();
        }
    }
}

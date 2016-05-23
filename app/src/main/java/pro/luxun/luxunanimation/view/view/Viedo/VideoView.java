package pro.luxun.luxunanimation.view.view.Viedo;

import android.content.Context;
import android.media.MediaCodec;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
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
import com.google.android.exoplayer.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer.util.PlayerControl;
import com.nineoldandroids.animation.Animator;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.SeekBarTouchStart;
import org.androidannotations.annotations.SeekBarTouchStop;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.List;
import java.util.concurrent.TimeUnit;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.JsonUtils;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.utils.SimpleAnimationListener;
import pro.luxun.luxunanimation.utils.Utils;
import pro.luxun.luxunanimation.view.view.Danmaku.DanmakuView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by wufeiyang on 16/5/9.
 */

@EViewGroup(R.layout.video)
public class VideoView extends FrameLayout implements ExoPlayer.Listener {

    private static final int K = 1024;
    private static final int TIME_ANIMATION = 250;

    private MediaCodecVideoTrackRenderer mVideoRender;
    private MediaCodecAudioTrackRenderer mAudioRender;
    private ExoPlayer mPlayer = ExoPlayer.Factory.newInstance(2);

    private PlayerControl mPlayerControl;
    private Subscription mUiTimer;
    private String userAgent;

    private boolean isHudConutDis = false;
    private int mHudVisableTime = 10;
    private AppCompatActivity mActivity;

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

    @StringRes(R.string.video_time)
    String mVideoTime;

    public VideoView(Context context) {
        super(context);
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        setKeepScreenOn(true);

        userAgent = "android/" + BuildConfig.VERSION_NAME;

        mPlayer.addListener(this);
        mPlayerControl = new PlayerControl(mPlayer);

        mActivity = (AppCompatActivity) getContext();

        startUitimer();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState){
            case PlaybackState.STATE_PLAYING:
                mProgressWheel.setVisibility(GONE);
                hideSystemUI();
                break;
            case PlaybackState.STATE_BUFFERING:
                break;
            case PlaybackState.STATE_STOPPED:
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

    @CheckedChange(R.id.play_btn)
    void onPlayCheckedChange(CompoundButton playBtn, boolean isChecked){
        if(isChecked){
            mPlayerControl.pause();
        }else {
            mPlayerControl.start();
        }
    }

    @SeekBarProgressChange(R.id.seek_bar)
    void onSeekBarProgress(){
        mTime.setText(String.format(mVideoTime, Utils.videoTimeFormat(mPlayer.getCurrentPosition())
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
        startUitimer();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("touch", "dispatch_down");
                isHudConutDis = false;
                mHudVisableTime = 10;
                showSystemUI();
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

        Uri uri = Uri.parse(url);

        DefaultHttpDataSource httpDataSource = new DefaultHttpDataSource(userAgent, null);
        httpDataSource.setRequestProperty("Referer", RetrofitClient.URL_REFERER);

        ExtractorSampleSource sampleSource = new ExtractorSampleSource(uri, httpDataSource, new DefaultAllocator(5 * K), 5 * K * K, new Mp4Extractor());

        mVideoRender = new MediaCodecVideoTrackRenderer(sampleSource, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT, 5 * K);
        mAudioRender = new MediaCodecAudioTrackRenderer(sampleSource);

        mPlayer.prepare(mVideoRender, mAudioRender);


    }

    public void initDanmaku(String title, String cur){
        RetrofitClient.getApiService().getDm(RetrofitClient.URL_DM + Utils.encodeURIComponent("lx:" + title + cur))
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
                        mDanmakuView.initDanmaku(JsonUtils.parserDanmaku(danmakulists));
                    }
                });
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

    public void startPlayer(){
        mPlayer.sendMessage(mVideoRender, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, mSurfaceView.getHolder().getSurface());
        mPlayer.setPlayWhenReady(true);
    }

    public void pausePlayer(){
        mPlayerControl.pause();
        stopUiTimer();
    }

    public void resumePlayer(){
        startUitimer();
    }

    public void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }

        stopUiTimer();
    }
}

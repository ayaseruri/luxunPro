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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.pnikosis.materialishprogress.ProgressWheel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.SeekBarProgressChange;
import org.androidannotations.annotations.SeekBarTouchStart;
import org.androidannotations.annotations.SeekBarTouchStop;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.concurrent.TimeUnit;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.LocalDisplay;
import pro.luxun.luxunanimation.utils.RxUtils;
import pro.luxun.luxunanimation.utils.Utils;
import rx.Observable;
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

    private GestureDetector mGestureDetector;
    private WindowManager.LayoutParams mWindowParams;
    private PlayerControl mPlayerControl;
    private boolean isHudVisible = true;
    private Subscription mUiTimer;
    private String userAgent;

    @ViewById(R.id.surface_view)
    SurfaceView mSurfaceView;
    @ViewById(R.id.light)
    VideoSide mLightView;
    @ViewById(R.id.sound)
    VideoSide mSoundView;
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

        Window window = ((AppCompatActivity) getContext()).getWindow();
        mWindowParams = window.getAttributes();

        mGestureDetector = new GestureDetector(getContext(), new VideoGesture());
        userAgent = "android/" + BuildConfig.VERSION_NAME;

        mPlayer.addListener(this);
        mPlayerControl = new PlayerControl(mPlayer);

        startUitimer();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState){
            case PlaybackState.STATE_PLAYING:
                isHudVisible = false;
                mProgressWheel.setVisibility(GONE);
                YoYo.with(Techniques.FadeOut).duration(TIME_ANIMATION).playOn(mHud);
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

    private class VideoGesture extends GestureDetector.SimpleOnGestureListener{

        private float mFirstDownY;

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            isHudVisible = !isHudVisible;
            Techniques techniques;
            if(isHudVisible){
                techniques = Techniques.FadeIn;
            }else {
                techniques = Techniques.FadeOut;
            }
            YoYo.with(techniques).duration(TIME_ANIMATION).playOn(mHud);
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            mFirstDownY = e.getY();
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            if(e1.getX() > LocalDisplay.SCREEN_HEIGHT_PIXELS / 2){ //开始调节屏幕亮度
//                changeLight((mFirstDownY - e2.getY()) / 32.0f / mLightView.getMax());
//            }else {//开始调节声音大小
//
//            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    public void initPlayer(String title, String url){
        Log.d("video_url", url);

        Context context = getContext();
        if(context instanceof AppCompatActivity){
            final AppCompatActivity activity = (AppCompatActivity) context;
            mToolbar.setTitle(title);
            activity.setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
            ActionBar actionBar = activity.getSupportActionBar();
            if(null != actionBar){
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        Uri uri = Uri.parse(url);

        DefaultHttpDataSource httpDataSource = new DefaultHttpDataSource(userAgent, null);
        httpDataSource.setRequestProperty("Referer", RetrofitClient.URL_REFERER);

        ExtractorSampleSource sampleSource = new ExtractorSampleSource(uri, httpDataSource, new DefaultAllocator(5 * K), 5 * K * K, new Mp4Extractor());

        mVideoRender = new MediaCodecVideoTrackRenderer(sampleSource, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT, 5 * K);
        mAudioRender = new MediaCodecAudioTrackRenderer(sampleSource);

        mPlayer.prepare(mVideoRender, mAudioRender);
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

    @Touch(R.id.surface_view)
    void onTouch(MotionEvent e){
        mGestureDetector.onTouchEvent(e);
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
        stopUiTimer();
    }

    @SeekBarTouchStop(R.id.seek_bar)
    void onSeekBarTouchStop(){
        mPlayer.seekTo(mSeekBar.getProgress());
        startUitimer();
    }

    private void startUitimer(){
        mUiTimer = Observable.interval(1, TimeUnit.SECONDS).compose(RxUtils.applySchedulers())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        mSeekBar.setMax((int) mPlayer.getDuration());
                        mSeekBar.setProgress((int) mPlayer.getCurrentPosition());
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

    //这里传入的是变化量的百分比，例如：0.2，0.4
    private void changeLight(float addPercent){
        float brightness = getScreenBrightness();
        if (brightness <= 0.00f)
            brightness = 0.50f;
        if (brightness < 0.01f)
            brightness = 0.01f;

        brightness = brightness + addPercent;
        if(brightness < 0.01f){
            brightness = 0.01f;
        }else if(brightness > 1.0f){
            brightness = 1.0f;
        }
        setScreenBrightness(brightness);

        mLightView.setValue(brightness * mLightView.getMax());
    }

    private float getScreenBrightness(){
        return mWindowParams.screenBrightness;
    }

    private void setScreenBrightness(float f){
        mWindowParams.screenBrightness = f;
        ((AppCompatActivity) getContext()).getWindow().setAttributes(mWindowParams);
    }
}

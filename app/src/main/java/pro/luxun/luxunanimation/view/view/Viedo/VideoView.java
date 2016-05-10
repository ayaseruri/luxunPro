package pro.luxun.luxunanimation.view.view.Viedo;

import android.app.Activity;
import android.content.Context;
import android.media.MediaCodec;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.exoplayer.BuildConfig;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultHttpDataSource;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.LocalDisplay;

/**
 * Created by wufeiyang on 16/5/9.
 */

@EViewGroup(R.layout.video)
public class VideoView extends FrameLayout implements ExoPlayer.Listener {

    private static final int MB = 1024 * 1024;
    private MediaCodecVideoTrackRenderer mVideoRender;
    private MediaCodecAudioTrackRenderer mAudioRender;
    private ExoPlayer mPlayer = ExoPlayer.Factory.newInstance(2);

    private GestureDetector mGestureDetector;

    @ViewById(R.id.surface_view)
    SurfaceView mSurfaceView;
    @ViewById(R.id.light)
    VideoSide mLightView;
    @ViewById(R.id.sound)
    VideoSide mSoundView;

    public VideoView(Context context) {
        super(context);
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        setKeepScreenOn(true);
        mGestureDetector = new GestureDetector(getContext(), new VideoGesture());
    }

    public void initPlayer(String url){
        Uri uri = Uri.parse(url);

        DefaultHttpDataSource httpDataSource = new DefaultHttpDataSource("android/" + BuildConfig.VERSION_NAME, null);
        httpDataSource.setRequestProperty("Referer", RetrofitClient.URL_REFERER);

        ExtractorSampleSource sampleSource = new ExtractorSampleSource(uri, httpDataSource
                , new DefaultAllocator(2 * MB), 2 * MB, new Mp4Extractor());

        mVideoRender = new MediaCodecVideoTrackRenderer(getContext(), sampleSource, MediaCodecSelector.DEFAULT, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        mAudioRender = new MediaCodecAudioTrackRenderer(sampleSource, MediaCodecSelector.DEFAULT);

        mPlayer.prepare(mVideoRender, mAudioRender);
    }

    public void startPlayer(){
        mPlayer.sendMessage(mVideoRender, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, mSurfaceView.getHolder().getSurface());
        mPlayer.setPlayWhenReady(true);
    }

    public void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayWhenReadyCommitted() {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    private void changeLight(float addPercent){
        Window window = ((Activity) getContext()).getWindow();
        WindowManager.LayoutParams lpa = window.getAttributes();

        float brightness = lpa.screenBrightness;
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
        lpa.screenBrightness = brightness;
        window.setAttributes(lpa);
    }

    private class VideoGesture extends GestureDetector.SimpleOnGestureListener{

        private float mFirstDownY;
        private float mTotalY;

        @Override
        public boolean onDown(MotionEvent e) {
            mFirstDownY = e.getY();
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(e1.getX() > LocalDisplay.SCREEN_HEIGHT_PIXELS / 2){ //开始调节屏幕亮度

                mTotalY = mTotalY + distanceY;
                mLightView.setValue((int) Math.max(100, mTotalY / 4));
                changeLight(mLightView.getPercent());
            }else {//开始调节声音大小

            }


            return true;
        }
    }

    @Touch(R.id.surface_view)
    void onTouch(MotionEvent e){
        mGestureDetector.onTouchEvent(e);
    }
}

package pro.luxun.luxunanimation.view.view.Viedo;

import android.content.Context;
import android.media.MediaCodec;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.SurfaceView;
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
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.net.RetrofitClient;

/**
 * Created by wufeiyang on 16/5/9.
 */

@EViewGroup(R.layout.video)
public class VideoView extends FrameLayout implements ExoPlayer.Listener {

    private static final int MB = 1024 * 1024;
    private MediaCodecVideoTrackRenderer mVideoRender;
    private MediaCodecAudioTrackRenderer mAudioRender;

    private ExoPlayer mPlayer = ExoPlayer.Factory.newInstance(2);

    @ViewById(R.id.surface_view)
    SurfaceView mSurfaceView;

    public VideoView(Context context) {
        super(context);
    }

    public VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void init(){
        setKeepScreenOn(true);
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
}

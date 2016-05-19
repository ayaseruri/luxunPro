package pro.luxun.luxunanimation.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.global.IntentConstant;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.Utils;
import pro.luxun.luxunanimation.view.view.Viedo.VideoView;

@EActivity(R.layout.activity_video)
public class VideoActivity extends AppCompatActivity {

    @ViewById(R.id.video)
    VideoView mVideoView;

    @AfterViews
    void init(){
        String videoTitle = getIntent().getStringExtra(IntentConstant.INTENT_VIDEO_TITLE);
        String videoUrl = getIntent().getStringExtra(IntentConstant.INTENT_VIDEO_URL);

        videoUrl = RetrofitClient.URL_SOURCE + Utils.encodeURIComponent(videoTitle) + "/" + videoUrl;

        mVideoView.initPlayer(videoTitle, videoUrl);
        mVideoView.startPlayer();

        hideSystemUI();

        mVideoView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                mVideoView.setHudVisibility(visibility & View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
        });
    }

    @Click(R.id.video)
    void onVideo(){
        showSystemUI();
    }

    @Override
    protected void onResume() {
        mVideoView.resumePlayer();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mVideoView.pausePlayer();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mVideoView.releasePlayer();
        super.onDestroy();
    }

    private void showSystemUI(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void hideSystemUI(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN );
    }
}

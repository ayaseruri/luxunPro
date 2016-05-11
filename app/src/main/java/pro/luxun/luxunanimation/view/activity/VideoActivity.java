package pro.luxun.luxunanimation.view.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.global.IntentConstant;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.view.view.Viedo.VideoView;

@EActivity(R.layout.activity_video)
public class VideoActivity extends AppCompatActivity {

    @ViewById(R.id.video)
    VideoView mVideoView;


    @AfterViews
    void init(){
        String videoTitle = getIntent().getStringExtra(IntentConstant.INTENT_VIDEO_TITLE);
        String videoUrl = getIntent().getStringExtra(IntentConstant.INTENT_VIDEO_URL);

        videoUrl = RetrofitClient.URL_SOURCE + UrlEncode(videoTitle) + "/" + videoUrl;

        mVideoView.initPlayer(videoTitle, videoUrl);
        mVideoView.startPlayer();
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

package pro.luxun.luxunanimation.view.activity;

import android.util.Log;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.bean.MainJson;
import pro.luxun.luxunanimation.global.IntentConstant;
import pro.luxun.luxunanimation.net.RetrofitClient;
import pro.luxun.luxunanimation.utils.Utils;
import pro.luxun.luxunanimation.view.view.Viedo.VideoView;

@EActivity(R.layout.activity_video)
public class VideoActivity extends BaseActivity {

    @ViewById(R.id.video)
    VideoView mVideoView;

    @Override
    protected void onResume() {
        MainJson.UpdatingEntity.SetsEntity setsEntity = getIntent().getParcelableExtra(IntentConstant.INTENT_VIDEO_SET_ENTITY);
        String videoTitle = setsEntity.getTitle();
        String orgTitle = setsEntity.getOrgTitle();
        String videoUrl = setsEntity.getUrl();
        String videCur = setsEntity.getSet();

        if(!videoUrl.startsWith("http://")){
            videoUrl = RetrofitClient.URL_SOURCE + Utils.encodeURIComponent(orgTitle + "/" + videoUrl);
        }

        mVideoView.initPlayer(orgTitle, videoUrl);
        mVideoView.initDanmaku(videoTitle, videCur);

        mVideoView.resumePlayer();
        Log.d("VideoActivity", "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        mVideoView.pausePlayer();
        Log.d("VideoActivity", "onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mVideoView.releasePlayer();
        Log.d("VideoActivity", "onDestroy");
        super.onDestroy();
    }
}

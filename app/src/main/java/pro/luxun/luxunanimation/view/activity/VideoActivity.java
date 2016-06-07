package pro.luxun.luxunanimation.view.activity;

import android.support.v7.app.AppCompatActivity;
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
public class VideoActivity extends AppCompatActivity {

    @ViewById(R.id.video)
    VideoView mVideoView;

    @Override
    protected void onResume() {
        super.onResume();
        MainJson.UpdatingEntity.SetsEntity setsEntity = getIntent().getParcelableExtra(IntentConstant.INTENT_VIDEO_SET_ENTITY);
        String videoTitle = setsEntity.getTitle();
        String orgTitle = setsEntity.getOrgTitle();
        String videoUrl = setsEntity.getUrl();
        String videCur = setsEntity.getSet();

        videoUrl = RetrofitClient.URL_SOURCE + Utils.encodeURIComponent(orgTitle + "/" + videoUrl);

        mVideoView.initPlayer(orgTitle, videoTitle, videoUrl, videCur);
        Log.d("VideoActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.releasePlayer();
        Log.d("VideoActivity", "onPause");
    }
}

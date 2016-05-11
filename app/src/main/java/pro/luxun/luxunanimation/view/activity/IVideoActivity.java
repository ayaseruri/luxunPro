package pro.luxun.luxunanimation.view.activity;

/**
 * Created by wufeiyang on 16/5/11.
 */
public interface IVideoActivity {
    void onPerpareVideo();
    void onPerpareVideoFailed();
    void onPerpareVideoSuccess();

    void onPlayingTimeChanged(String formatedTime);
    void onPauseVideo();
    void onRestartVideo();
    void onStartVideo();

    void onEndVideo();
}

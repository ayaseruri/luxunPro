package ykooze.ayaseruri.codesslib.net.download;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public interface DownloadListener {
    void onStart();
    void onProgress(long current, long total);
    void onSuccess();
    void onError(Throwable throwable);
}

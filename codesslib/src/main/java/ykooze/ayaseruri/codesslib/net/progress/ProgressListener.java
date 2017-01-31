package ykooze.ayaseruri.codesslib.net.progress;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public interface ProgressListener{
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
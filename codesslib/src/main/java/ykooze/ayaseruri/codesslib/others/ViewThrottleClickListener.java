package ykooze.ayaseruri.codesslib.others;

import android.view.View;

import java.util.Calendar;

/**
 * 一个可以防止重复点击的 onclicklistener
 * Created by wufeiyang on 16/6/18.
 */
public abstract class ViewThrottleClickListener implements View.OnClickListener {
    private static final int THROTTLE_TIME_DEFAULT = 1000; // 1s

    private long mLastClickTime = 0, mThrottleTime = THROTTLE_TIME_DEFAULT;

    public ViewThrottleClickListener(long throttleTime) {
        mThrottleTime = throttleTime;
    }

    public long getThrottleTime() {
        return mThrottleTime;
    }

    public abstract void throttleClick(View view);

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - mLastClickTime > getThrottleTime()) {
            mLastClickTime = currentTime;
            throttleClick(v);
        }
    }
}

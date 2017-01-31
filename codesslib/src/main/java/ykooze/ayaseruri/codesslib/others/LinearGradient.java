package ykooze.ayaseruri.codesslib.others;

import android.graphics.Color;

/**
 * Created by wufeiyang on 16/9/19.
 */

public class LinearGradient {
    private int mStartColor;
    private int mEndColor;

    public LinearGradient(int startColor, int endColor) {
        this.mStartColor = startColor;
        this.mEndColor = endColor;
    }

    public void setStartColor(int startColor) {
        this.mStartColor = startColor;
    }

    public void setEndColor(int endColor) {
        this.mEndColor = endColor;
    }

    public int getColor(float radio) {
        int redStart = Color.red(mStartColor);
        int blueStart = Color.blue(mStartColor);
        int greenStart = Color.green(mStartColor);
        int redEnd = Color.red(mEndColor);
        int blueEnd = Color.blue(mEndColor);
        int greenEnd = Color.green(mEndColor);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        return Color.argb(255, red, greed, blue);
    }
}

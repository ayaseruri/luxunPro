package pro.luxun.luxunanimation.view.view.Viedo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DrawableRes;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.utils.LocalDisplay;

/**
 * Created by wufeiyang on 16/5/10.
 */
@EViewGroup(R.layout.video_side)
public class VideoSide extends FrameLayout {

    @ViewById(R.id.value)
    ImageView mValueBar;
    @DrawableRes(R.drawable.video_side_bg)
    Drawable mBackground;

    private LayoutParams mValueBarParams;
    private int mMax = 100;
    private int mDpHeight;

    public VideoSide(Context context) {
        super(context);
    }

    public VideoSide(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VideoSide);
        mDpHeight = array.getDimensionPixelSize(R.styleable.VideoSide_height_dp, 100);
        array.recycle();
    }

    @AfterViews
    void init(){
        setBackground(mBackground);
        mValueBarParams = (LayoutParams) mValueBar.getLayoutParams();
    }

    public void setValue(float value){
        mValueBarParams.height = (int) Math.min(1.0f * mDpHeight * value, mDpHeight - LocalDisplay.dp2px(4));
        mValueBar.requestLayout();
    }

    public float getPercent(){
        return mValueBarParams.height * 1.0f / mDpHeight;
    }

    public void setMax(int max){
        mMax = max;
    }

    public int getMax() {
        return mMax;
    }
}

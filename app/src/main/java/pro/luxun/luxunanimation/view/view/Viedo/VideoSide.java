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
        mDpHeight = mDpHeight - 2 * LocalDisplay.dp2px(4);
        array.recycle();
    }

    @AfterViews
    void init(){
        setBackground(mBackground);
        mValueBarParams = (LayoutParams) mValueBar.getLayoutParams();
    }

    //传入值相对于是max而言，例如：max ＝ 100，则传入30，40等小于100的整数
    public void setValue(float value){
        if(value < mMax){
            mValueBarParams.height = (int) (mDpHeight * value / mMax);
        }else {
            mValueBarParams.height = LayoutParams.MATCH_PARENT;
        }
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
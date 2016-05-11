package pro.luxun.luxunanimation.view.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.res.ColorRes;

import pro.luxun.luxunanimation.R;
import pro.luxun.luxunanimation.utils.TranslucentStatusHelper;

/**
 * Created by wufeiyang on 16/5/7.
 */
@EViewGroup
public class TranslucentStatusBarLinearLayout extends LinearLayout{

    @ColorRes(R.color.colorPrimaryDark)
    int mColorPrimaryDark;

    private int mStatusColor;

    public TranslucentStatusBarLinearLayout(Context context) {
        super(context);
    }

    public TranslucentStatusBarLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TranslucentStatusBarLinearLayout);
        mStatusColor = array.getColor(R.styleable.TranslucentStatusBarLinearLayout_status_bar_color, mColorPrimaryDark);
        array.recycle();
    }

    @AfterViews
    void init(){
        setOrientation(VERTICAL);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            setFitsSystemWindows(false);

            Context context = getContext();
            if(context instanceof Activity){
                TranslucentStatusHelper.translucentStatus((Activity) context);
                ImageView statusBar = new ImageView(context);
                statusBar.setBackgroundColor(mStatusColor);
                addView(statusBar, 0, new LayoutParams(LayoutParams.MATCH_PARENT, TranslucentStatusHelper.getStatusBarHeight(context)));
            }
        }
    }
}
